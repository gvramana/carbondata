/**
 * 
 */
package com.huawei.unibi.molap.datastorage.store.filesystem;

import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hdfs.DistributedFileSystem;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.util.MolapCoreLogEvent;

/**
 * @author R00900208
 *
 */
public class HDFSMolapFile implements MolapFile
{
    private FileStatus fileStatus;
    
    private FileSystem fs;
    
    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(HDFSMolapFile.class.getName());
    
    public HDFSMolapFile(String filePath)
    {
        filePath = filePath.replace("\\", "/");
        Path path=new Path(filePath);
        try
        {
            fs = path.getFileSystem(FileFactory.getConfiguration());
            fileStatus = fs.getFileStatus(path);
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
        }
    }
    
    public HDFSMolapFile(Path path)
    {
        try
        {
            fs = path.getFileSystem(FileFactory.getConfiguration());
            fileStatus = fs.getFileStatus(path);
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
        }
    }
    
    public HDFSMolapFile(FileStatus fileStatus)
    {
        this.fileStatus = fileStatus;
    }
    
    
    @Override
    public boolean createNewFile()
    {
        Path path = fileStatus.getPath();
        try
        {
            return fs.createNewFile(path);
        }
        catch(IOException e)
        {
            return false;
        }

    }

    @Override
    public String getAbsolutePath()
    {
        return fileStatus.getPath().toString();
    }

    @Override
    public MolapFile[] listFiles(final MolapFileFilter fileFilter)
    {
        FileStatus[] listStatus = null;
        try
        {
            if(null!=fileStatus && fileStatus.isDir())
            {
                Path path = fileStatus.getPath();
                listStatus = path.getFileSystem(FileFactory.getConfiguration()).listStatus(path, new PathFilter()
                {
                    
                    @Override
                    public boolean accept(Path path)
                    {
                        
                        return fileFilter.accept(new HDFSMolapFile(path));
                    }
                });
            }
            else
            {
                return null;
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
            return new MolapFile[0];
        }
        
        return getFiles(listStatus);
    }

    @Override
    public String getName()
    {
        return fileStatus.getPath().getName();
    }

    @Override
    public boolean isDirectory()
    {
        return fileStatus.isDir();
    }

    @Override
    public boolean exists()
    {
        try
        {
            if(null!=fileStatus)
            {
                fs = fileStatus.getPath().getFileSystem(FileFactory.getConfiguration());
                return fs.exists(fileStatus.getPath());
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
        }
        return false;
    }

    @Override
    public String getCanonicalPath()
    {
        return getAbsolutePath();
    }

    @Override
    public MolapFile getParentFile()
    {
        return new HDFSMolapFile(fileStatus.getPath().getParent());
    }

    @Override
    public String getPath()
    {
        return getAbsolutePath();
    }

    @Override
    public long getSize()
    {
        return fileStatus.getLen();
    }
    
    public boolean renameTo(String changetoName)
    {
        FileSystem fs;
        try
        {
            fs = fileStatus.getPath().getFileSystem(FileFactory.getConfiguration());
            return fs.rename(fileStatus.getPath(), new Path(changetoName));
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
            return false;
        }
    }

    public boolean delete()
    {
        FileSystem fs;
        try
        {
            fs = fileStatus.getPath().getFileSystem(FileFactory.getConfiguration());
            return fs.delete(fileStatus.getPath(), true);
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
            return false;
        }
    }

    @Override
    public MolapFile[] listFiles()
    {

        FileStatus[] listStatus = null;
        try
        {
            if(null!=fileStatus && fileStatus.isDir())
            {
                Path path = fileStatus.getPath();
                listStatus = path.getFileSystem(FileFactory.getConfiguration()).listStatus(path);
            }
            else
            {
                return null;
            }
        }
        catch(IOException ex)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+ex.getMessage());
            return new MolapFile[0];
        }
        
        return getFiles(listStatus);
    }

    /**
     * @param listStatus
     * @return
     */
    private MolapFile[] getFiles(FileStatus[] listStatus)
    {
        if(listStatus == null)
        {
            return  new MolapFile[0];
        }
        
        MolapFile[] files = new MolapFile[listStatus.length];
        
        for(int i = 0;i < files.length;i++)
        {
            files[i] = new HDFSMolapFile(listStatus[i]);
        }
        return files;
    }
    
    @Override
    public boolean mkdirs()
    {
        Path path = fileStatus.getPath();
        try
        {
            return fs.mkdirs(path);
        }
        catch(IOException e)
        {
            return false;
        }
    }

    @Override
    public long getLastModifiedTime()
    {
        return fileStatus.getModificationTime();
    }

    @Override
    public boolean setLastModifiedTime(long timestamp)
    {
        try
        {
            fs.setTimes(fileStatus.getPath(), timestamp, timestamp);
        }
        catch(IOException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean renameForce(String changetoName)
    {
        FileSystem fs;
        try
        {
            fs = fileStatus.getPath().getFileSystem(FileFactory.getConfiguration());
            if(fs instanceof DistributedFileSystem)
            {
            ((DistributedFileSystem)fs).rename(fileStatus.getPath(), new Path(changetoName),org.apache.hadoop.fs.Options.Rename.OVERWRITE);
            return true;
            }
            else{
                return false;
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Exception occured"+e.getMessage());
            return false;
        }
    }
}
