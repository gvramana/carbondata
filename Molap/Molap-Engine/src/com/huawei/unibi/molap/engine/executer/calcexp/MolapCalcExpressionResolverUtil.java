/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcAIRTtLWBkMMN+iqJ62JNQb/MYFaBoemC1VlrU
n+vkOQ8CipVZItgvA6SoPZ1T24XARi4nhKHIUHDdOEMQHzSrSCpXt1HWS/C5DlTRQyPqyhw4
rd0tnpAxPfLJ3BRN4IxsActCQecz2U8I9Zh/q4Ixkxz7nlI0D8tfFzr5TqilWQ==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.executer.calcexp;

import java.util.List;

import com.huawei.unibi.molap.engine.executer.calcexp.impl.CalcExpressionModel;
import com.huawei.unibi.molap.engine.schema.metadata.Pair;
//import com.huawei.unibi.molap.metadata.MolapMetadata;
//import com.huawei.unibi.molap.metadata.MolapMetadata.Cube;
import com.huawei.unibi.molap.metadata.MolapMetadata.Measure;
import com.huawei.unibi.molap.olap.Exp;

/**
 * Utility to resolve expressions to Molap understanble functions.
 * @author R00900208
 *
 */
public final class MolapCalcExpressionResolverUtil
{
    private MolapCalcExpressionResolverUtil()
    {
        
    }
    
//    /**
//     * 
//     * @param query
//     * @param Id
//     * @param msrs
//     * @return
//     */
//    public static MolapCalcFunction createCalcExpressions(Query query,String Id,List<Measure> msrs)
//    {
//        Formula[] formulas = query.getFormulas();
//        Formula formulaReq = null;
//        for(Formula formula : formulas)
//        {
//            if(formula.getIdentifier().toString().equals(Id))
//            {
//                formulaReq = formula;
//                break;
//            }
//        }
//        
//        if(null == formulaReq)
//        {
//            return null;
//        }
//        return createCalcExpressions(formulaReq.getExpression(), msrs);
//    }
    
//    /**
//     * 
//     * @param query
//     * @param Id
//     * @return
//     */
//    public static CalculatedMeasure createCalcMeasure(Query query,String Id)
//    {
//        Formula[] formulas = query.getFormulas();
//        Formula formulaReq = null;
//        for(Formula formula : formulas)
//        {
//            if(getBaseToken(formula.getIdentifier().toString()).equals(Id))
//            {
//                formulaReq = formula;
//                break;
//            }
//        }
//        
//        if(null == formulaReq)
//        {
//            return null;
//        }
//        CalculatedMeasure calculatedMeasure = new CalculatedMeasure(formulaReq.getExpression(), Id);
//        
//        return calculatedMeasure;
//    }
    
   /* *//**
     * Get all the measures underlying in calculated measures.
     * @param msrs
     * @param exp
     * @param queryCube
     *//*
    public static void getMeasuresFromCalcMeasures(List<MolapMetadata.Measure> msrs,Exp exp,Cube queryCube)
    {
        List<MolapMetadata.Measure> msrsLocal = new ArrayList<MolapMetadata.Measure>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        
        parseExpression(msrsLocal, exp, queryCube);
        
        List<MolapMetadata.Measure> msrsLocalUnq = new ArrayList<MolapMetadata.Measure>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        
        for(int i = 0;i < msrsLocal.size();i++)
        {
            MolapMetadata.Measure measure = msrsLocal.get(i);
            boolean found = false;
            for(int j = 0;j < msrs.size();j++)
            {
                if(msrs.get(j).getName().equals(measure.getName()))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                msrsLocalUnq.add(measure);
            }
        }
        
        msrs.addAll(msrsLocalUnq);
        
    }*/

//    private static void parseExpression(List<MolapMetadata.Measure> msrs,Exp exp,Cube queryCube)
//    {
//        if(exp instanceof ResolvedFunCall)
//        {
//            ResolvedFunCall funCall = (ResolvedFunCall)exp;
//            Exp[] args = funCall.getArgs();
//            for(int i = 0;i < args.length;i++)
//            {
//                parseExpression(msrs, args[i],queryCube);
//            }
//        }
//        else if(exp instanceof MemberExpr)
//        {
//            MemberExpr expr = (MemberExpr)exp;
//            if(expr.getMember() instanceof RolapCalculatedMeasure)
//            {
//                RolapCalculatedMeasure calMsr = (RolapCalculatedMeasure)expr.getMember();
//                parseExpression(msrs, calMsr.getExpression(),queryCube);
//            }
//            else if(expr.getMember() instanceof RolapBaseCubeMeasure)
//            {
//                RolapBaseCubeMeasure cubeMeasure = (RolapBaseCubeMeasure)expr.getMember();
//                
//                Measure measure = queryCube.getMeasure(queryCube.getFactTableName(), cubeMeasure.getName());
//                if(!msrs.contains(measure))
//                {
//                    msrs.add(measure);
//                }
//            }
//
//        }
//    }

    
    /**
     * 
     * @param member
     * @return
     */
   /* private static String getBaseToken(String member) 
    {
        return member.substring(member.lastIndexOf('[')+1, 
            member.lastIndexOf(']'));
    }*/
    
    /**
     * 
     * @param exp
     * @param msrs
     * @return
     */
    public static MolapCalcFunction createCalcExpressions(Exp exp,List<Measure> msrs)
    {
 
        CalcExpressionModel model = new CalcExpressionModel();
        model.setMsrsList(msrs);
        Pair<MolapCalcFunction, Exp> pair = getMolapExpression(exp);
        if(pair.getKey() == null)
        {
            return null;
        }
        pair.getKey().compile(model, pair.getValue());
        return pair.getKey();
    }

    /**
     * @param exp
     * @return
     */
    private static Pair<MolapCalcFunction, Exp> getMolapExpression(Exp exp)
    {
        MolapCalcFunction calcFunction = null;
//        if(exp instanceof ResolvedFunCall)
//        {
//            ResolvedFunCall funCall = (ResolvedFunCall)exp;
//            CalCExpressionName calcExpr = MolapCalcExpressionResolverUtil.getCalcExpr(funCall.getFunName());
//            calcFunction = CalcExpressionFactory.getInstance().getCalcFunction(calcExpr);
//
//        } 
//        else if(exp instanceof MemberExpr)
//        {
//            MemberExpr memberExpr = (MemberExpr)exp;
//            if(memberExpr.getMember() instanceof RolapCalculatedMeasure)
//            {
//                RolapCalculatedMeasure calMsr = (RolapCalculatedMeasure)memberExpr.getMember();
//                return getMolapExpression(calMsr.getExpression());
//            }
//            else
//            {
//                calcFunction = CalcExpressionFactory.getInstance().getSingleCalcFunction();
//            }
//        }
//        else if(exp instanceof Literal)
//        {
//            calcFunction = new MolapConstCalcFunction();
//        }
        return new Pair<MolapCalcFunction, Exp>(calcFunction, exp);
    }
    
    
/*
    *//**
     * 
     * @param name
     * @return
     *//*
    public static CalCExpressionName getCalcExpr(String name)
    {
        CalCExpressionName[] values = CalCExpressionName.values();
        
        for(CalCExpressionName expressionName : values)
        {
            if(expressionName.getName().equals(name))
            {
                return expressionName;
            }
        }
        return null;
    }*/
    

    
}
