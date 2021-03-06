package com.huawei.unibi.molap.engine.expression.arithmetic;

import com.huawei.unibi.molap.engine.expression.DataType;
import com.huawei.unibi.molap.engine.expression.Expression;
import com.huawei.unibi.molap.engine.expression.ExpressionResult;
import com.huawei.unibi.molap.engine.expression.exception.FilterUnsupportedException;
import com.huawei.unibi.molap.engine.molapfilterinterface.ExpressionType;
import com.huawei.unibi.molap.engine.molapfilterinterface.RowIntf;

public class SubstractExpression  extends BinaryArithmeticExpression 
{

    private static final long serialVersionUID = -8304726440185363102L;

    public SubstractExpression(Expression left, Expression right)
    {
        super(left, right);
    }

    @Override
    public ExpressionResult evaluate(RowIntf value) throws FilterUnsupportedException
    {
        ExpressionResult subtractExprLeftRes = left.evaluate(value);
        ExpressionResult subtractExprRightRes = right.evaluate(value);
        ExpressionResult val1 = subtractExprLeftRes;
        ExpressionResult val2 = subtractExprRightRes;
        if(subtractExprLeftRes.isNull() || subtractExprRightRes.isNull())
        {
            subtractExprLeftRes.set(subtractExprLeftRes.getDataType(), null);
            return subtractExprLeftRes;
        }
        if(subtractExprLeftRes.getDataType() != subtractExprRightRes.getDataType())
        {
            if(subtractExprLeftRes.getDataType().getPresedenceOrder() < subtractExprRightRes.getDataType().getPresedenceOrder())
            {
                val2 = subtractExprLeftRes;
                val1 = subtractExprRightRes;
            }
        }
        switch(val1.getDataType())
        {
            case StringType:
            case DoubleType:
                subtractExprRightRes.set(DataType.DoubleType, val1.getDouble()-val2.getDouble());
                break;
          case IntegerType:
            subtractExprRightRes.set(DataType.IntegerType, val1.getInt()-val2.getInt());
            break;
        default:
            throw new FilterUnsupportedException("Incompatible datatype for applying Add Expression Filter "
                    + subtractExprLeftRes.getDataType());
        }
        return subtractExprRightRes;
    }

    @Override
    public ExpressionType getFilterExpressionType()
    {
        return ExpressionType.SUBSTRACT;
    }

    @Override
    public String getString()
    {
        return "Substract(" + left.getString() + ',' + right.getString() + ')';
    }
}
