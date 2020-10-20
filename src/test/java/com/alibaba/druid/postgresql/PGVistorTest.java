package com.alibaba.druid.postgresql;

import java.util.List;

import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import org.junit.Test;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.alibaba.druid.sql.parser.SQLParserFeature;

/**
 * Created by wangcheng on 8/2/19.
 */
public class PGVistorTest {

    @Test
    public void testVisitor() {
        String sql = "INSERT INTO \n" + "  ${DWFXQMART}.FXQ_B_ACCT_TX\n" + "  ( \n" + "     Account_Num\n"
                + "    ,Account_Modifier_Num\n" + "    ,Cust_Acct_Num\n" + "    ,Cust_No\n" + "    ,Account_Name\n"
                + "    ,Acct_Type\n" + "    ,Acct_Char\n" + "    ,Acct_Sts\n" + "    ,Dep_Type\n" + "    ,Open_Org\n"
                + "    ,Open_Org_Name\n" + "    ,FXQ_Org\n" + "    ,FXQ_Org_Name\n" + "    ,Open_Dt\n"
                + "    ,Clsd_Dt\n" + "    ,Mature_Dt\n" + "    ,Open_Ibs_Dt\n" + "    ,Last_Tx_Dt\n"
                + "    ,Avail_Bal\n" + "    ,Rmb_Avail_Bal\n" + "    ,Acct_Ind\n" + "    ,Open_Org_Districts\n"
                + "    ,Card_Ind\n" + "    ,Term_Curr_Ind\n" + "    ,Comm_Context\n" + "    ,Acct_Source\n"
                + "    ,Sale_Product_Id\n" + "    ,Fund_Use_CD\n" + "    ,Cust_Type_CD\n" + "    ,Data_Dt\n"
                + "    ,Currency\t\t\t\t\t----币种 20151111 lijs\n" + "  )\n" + "SELECT\n"
                + "     Account_Num          ----系统账号\n" + "    ,Account_Modifier_Num ----账号修饰符\n"
                + "    ,Cust_Acct_Num        ----客户账号\n" + "    ,Cust_No              ----客户编号\n"
                + "    ,Account_Name         ----账户名称\n" + "    ,Acct_Type            ----账户类型\n"
                + "    ,Acct_Char            ----账户性质\n" + "    ,Acct_Sts             ----账户状态\n"
                + "    ,Dep_Type             ----存款种类\n" + "    ,Open_Org             ----开户机构\n"
                + "    ,Org_Name             ----机构名称\n" + "    ,FXQ_Org\n"
                + "    ,FXQ_Org_Name                              \n" + "    ,Open_Dt              ----开户日期\n"
                + "    ,Clsd_Dt                    \t\t\t\t\t\t\t----销户日期\n"
                + "    ,Mature_Dt            \t\t\t\t\t\t\t----到期日期\n"
                + "    ,Open_Ibs_Dt          \t\t\t\t\t\t\t----开通网银时间\n"
                + "    ,Last_Tx_Dt           \t\t\t\t\t\t\t----最近交易日期\n" + "    ,Avail_Bal            \t----账户余额\n"
                + "    ,Rmb_Avail_Bal       \t----账户余额\n" + "    ,Acct_Ind             \t\t\t\t\t\t\t----对公对私标志\n"
                + "    ,District_Cd          \t\t\t\t\t\t\t----行政区划代码\n"
                + "    ,Card_Ind             \t\t\t\t\t\t\t----卡标志\n"
                + "    ,Term_Curr_Ind        \t\t\t\t\t\t\t----定活存标志\n"
                + "    ,Comm_Context         \t\t\t\t\t\t\t----备注\n"
                + "    ,Acct_Source          \t\t\t\t\t\t\t----账户来源\n"
                + "    ,Sale_Product_Id      \t\t\t\t\t\t\t----可售产品编号\n"
                + "    ,Fund_Use_CD          \t\t\t\t\t\t\t----资金用途代码\n"
                + "    ,Cust_Type_CD         \t\t\t\t\t\t\t----客户类型代码\n" + "    ,Data_Dt\n"
                + "    ,Currency                          ----币种 20151111 lijs\n" + "FROM\n" + "(\n" + "    SELECT\n"
                + "         P1.Account_Num          ----系统账号\n" + "        ,P1.Account_Modifier_Num ----账号修饰符\n"
                + "        ,P1.Cust_Acct_Num        ----客户账号\n" + "        ,P1.Cust_No              ----客户编号\n"
                + "        ,P1.Account_Name         ----账户名称\n" + "        ,P1.Acct_Type            ----账户类型\n"
                + "        ,P1.Acct_Char            ----账户性质\n" + "        ,P1.Acct_Sts             ----账户状态\n"
                + "        ,P1.Dep_Type             ----存款种类\n" + "        ,P1.Open_Org             ----开户机构\n"
                + "        ,P2.Org_Name             ----机构名称\n" + "        ,case\n"
                + "             when P1.Open_Org_END=Coalesce(trim(P6.First_Org_Num),'A')||'000000' then P6.Fxq_Center_Org_Num--added by suyq 20160322 二级行集中的一级分行业务归到反洗钱中心\n"
                + "             when P2.Inter_Org_Level='08' and substr(P2.Org_Type_Cd,1,3) not in ('500','501','502') and P1.Open_Org<>'010000000' then P2.Org_Num \n"
                + "             when (P2.Org_Num_L1 is not null and trim(P2.Org_Num_L1)<>'' and P6.Fxq_Center_Org_Num is not null) then P6.Fxq_Center_Org_Num\n"
                + "             when P2.Org_Num_L1 is not null and trim(P2.Org_Num_L1)<>'' and P6.Fxq_Center_Org_Num is  null  then   P2.Org_Num_L1\n"
                + "             when P7.Org_Num  is not null and trim(P7.Org_Num)<>'' then P7.Org_Num\n"
                + "             when P8.Org_Num  is not null and trim(P8.Org_Num)<>'' then P8.Org_Num\n"
                + "            -- else case when P1.Acct_Source ='5'  then '310000000' else '110000000' end --20161119 CYL\n"
                + "            else case when P1.DETAIL_SOURCE ='03'  then '310000000' else '110000000' end\n"
                + "         end  as FXQ_Org\n" + "        ,case \n"
                + "             when P1.Open_Org_END=Coalesce(trim(P6.First_Org_Num),'A')||'000000' then P6.Fxq_Center_Org_Name--added by suyq 20160322 二级行集中的一级分行业务归到反洗钱中心\n"
                + "             when P2.Inter_Org_Level='08' and substr(P2.Org_Type_Cd,1,3) not in ('500','501','502')  and P1.Open_Org<>'010000000' then P2.Org_Name  \n"
                + "             when P2.Org_Num_L1 is not null and trim(P2.Org_Num_L1)<>'' and P6.Fxq_Center_Org_Num is not null then P6.Fxq_Center_Org_Name\n"
                + "             when P2.Org_Num_L1 is not null and trim(P2.Org_Num_L1)<>'' and P6.Fxq_Center_Org_Num is  null  then   P2.Org_L1_Name\n"
                + "             when P7.Org_Num  is not null and trim(P7.Org_Num)<>'' then P7.Org_Name\n"
                + "             when P8.Org_Num  is not null and trim(P8.Org_Num)<>'' then P8.Org_Name\n"
                + "             --else case when P1.Acct_Source ='5'  then '上海分行' else '中国建设银行股份有限公司北京市分行' end--20161119 CYL\n"
                + "           else case when P1.DETAIL_SOURCE ='03'   then '上海分行' else '中国建设银行股份有限公司北京市分行' end\n"
                + "         end as FXQ_Org_Name                              \n"
                + "        ,P1.Open_Dt              ----开户日期\n" + "        ,case \n"
                + "             when P1.Clsd_Dt<=TO_DATE('1902-12-31', 'YYYY-MM-DD') then TO_DATE('2999-12-31', 'YYYY-MM-DD')              \n"
                + "             else P1.Clsd_Dt \n"
                + "         end as Clsd_Dt                    \t\t\t\t\t\t\t----销户日期\n"
                + "        ,P1.Mature_Dt            \t\t\t\t\t\t\t----到期日期\n"
                + "        ,P1.Open_Ibs_Dt          \t\t\t\t\t\t\t----开通网银时间\n"
                + "        ,P1.Last_Tx_Dt           \t\t\t\t\t\t\t----最近交易日期\n"
                + "        ,COALESCE(P1.Avail_Bal, 0) as Avail_Bal            \t----账户余额\n"
                + "        ,COALESCE(P1.Rmb_Avail_Bal, 0) as  Rmb_Avail_Bal       \t----账户余额\n"
                + "        ,P1.Acct_Ind             \t\t\t\t\t\t\t----对公对私标志\n"
                + "        ,P2.District_Cd          \t\t\t\t\t\t\t----行政区划代码\n"
                + "        ,P1.Card_Ind             \t\t\t\t\t\t\t----卡标志\n"
                + "        ,P1.Term_Curr_Ind        \t\t\t\t\t\t\t----定活存标志\n"
                + "        ,P1.Comm_Context         \t\t\t\t\t\t\t----备注\n"
                + "        ,P1.Acct_Source          \t\t\t\t\t\t\t----账户来源\n"
                + "        ,P3.Sale_Product_Id      \t\t\t\t\t\t\t----可售产品编号\n"
                + "        ,P4.Fund_Use_CD          \t\t\t\t\t\t\t----资金用途代码\n"
                + "        ,P5.Cust_Type_CD         \t\t\t\t\t\t\t----客户类型代码\n"
                + "        ,TO_DATE('${TXNDATE}', 'YYYY-MM-DD') as Data_Dt\n"
                + "        ,P1.Currency                          ----币种 20151111 lijs\n"
                + "        ,ROW_NUMBER() OVER(PARTITION BY P1.Account_Num,P1.Account_Modifier_Num,P1.Cust_Acct_Num) as rn\n"
                + "    FROM \n" + "        VT_FXQ_B_ACCT_TX_END  AS P1\n" + "      LEFT JOIN     \n"
                + "        VT_VB_ORG_INFO P2 --改用视图modified 20151112\n"
                + "      ON                                  ----MODIFY BY 20151023\n"
                + "         P1.Open_Org_END = P2.Org_Num\n"
                + "         AND P2.Data_Dt=TO_DATE('${TXNDATE}', 'YYYY-MM-DD') \n" + "      LEFT JOIN     \n"
                + "        VT_VB_ORG_INFO P7     /*用二级分行号关联*/\n" + "      ON                                  \n"
                + "         P1.Org_Num_L2 = P7.Org_Num\n"
                + "         AND P7.Data_Dt=TO_DATE('${TXNDATE}', 'YYYY-MM-DD')\n"
                + "         AND P7.Inter_Org_Level='08'    \n" + "      LEFT JOIN     \n"
                + "        VT_VB_ORG_INFO P8    /*用一级分行号关联*/\n" + "      ON                                  \n"
                + "         P1.Org_Num_L1 = P8.Org_Num\n"
                + "         AND P8.Data_Dt=TO_DATE('${TXNDATE}', 'YYYY-MM-DD')\n"
                + "         AND P8.Inter_Org_Level='08'  \n" + "      LEFT JOIN \n"
                + "        ${DWFXQVIEW}.FXQ_VP_FIRST_CENTER_ORG_RELA  P6   ---一级分行与反洗钱中心对照表\n" + "      ON\n"
                + "         substr(P2.Org_Num_L1,1,3)=P6.First_Org_Num \n" + "      LEFT JOIN\n"
                + "         VT_B_CORP_ACCT_PRODUCT_ID  P3  ---对公账户可售产品编号临时表\n" + "      ON\n"
                + "         P1.Account_Num  = P3.Account_Num       \n"
                + "         and P1.Account_Modifier_Num= P3.Account_Modifier_Num  \n" + "      LEFT JOIN \n"
                + "         VT_B_CORP_ACCT_FUND_USE_CD P4  ---对公账户资金用途代码临时表\n" + "      ON\n"
                + "         P1.Account_Num  = P4.Account_Num       \n"
                + "         and P1.Account_Modifier_Num= P4.Account_Modifier_Num     \n" + "      LEFT JOIN \n"
                + "         VT_B_CORP_ACCT_CUST_TYPE_CD P5 ---对公账户客户类型代码临时表\n" + "      ON\n"
                + "         P1.Account_Num  = P5.Account_Num       \n"
                + "         and P1.Account_Modifier_Num= P5.Account_Modifier_Num             \n" + "    WHERE \n"
                + "        1=1  -- P1.Cust_No <>''\n" + ") T WHERE rn=1\n"
                + "--    Qualify SUM(1) OVER(PARTITION BY P1.Account_Num,P1.Account_Modifier_Num,P1.Cust_Acct_Num ROWS UNBOUNDED PRECEDING) = 1 \n";

        String sql2 = "select a.b ,----aaaa \n a.c ----cccc \nfrom table a";
        String sql3 = "CREATE TABLE ecas_app.ecas_corp_loan_rmb_int\n" +
                "(\n" +
                "  yngyjg character varying(4), -- 贷款所属营业机构号\n" +
                "  jigomc character varying(80), -- 贷款所属营业机构中文名\n" +
                "  yjfhjg character varying(4), -- 一级分行机构号\n" +
                "  yjfhmc character varying(40), -- 一级分行机构名称\n" +
                "  huobdh character varying(2), -- 贷款的币种\n" +
                "  huobmc character varying(16), -- 币种名称\n" +
                "  kmuhao character varying(6), -- 贷款本金的科目号\n" +
                "  jiejuh character varying(16), -- 贷款借据号\n" +
                "  kehhao character varying(10), -- 贷款的客户号\n" +
                "  kehzwm character varying(80), -- 贷款的客户中文名\n" +
                "  wjdkfl character varying(20), -- 五级分类\n" +
                "  daoqrq character varying(8), -- 贷款的到期日\n" +
                "  lixihj numeric(38,6), -- 利息合计\n" +
                "  jieszh character varying(20), -- 贷款的结算账号\n" +
                "  zhhuye numeric(15,2), -- 账户余额\n" +
                "  djieye numeric(15,2), -- 冻结余额\n" +
                "  drhkzh character varying(20), -- 贷款的第二还款账号\n" +
                "  drzhye numeric(15,2), -- 第二还款账户余额\n" +
                "  benjhu character varying(20), -- 贷款的本金户\n" +
                "  lilvll numeric(20,7), -- 本金户的利率\n" +
                "  bjhyue numeric(15,2), -- 贷款本金户余额\n" +
                "  bjhjis numeric(20,2), -- 贷款本金户积数\n" +
                "  yjajlx numeric(15,2), -- 贷款本金户应加减利息\n" +
                "  bjhlix numeric(20,2), -- 贷款本金户利息\n" +
                "  qxhzhh character varying(20), -- 贷款欠息户\n" +
                "  qxhull numeric(9,7), -- 欠息户利率\n" +
                "  qxhyue numeric(15,2), -- 欠息户余额\n" +
                "  qxhjis numeric(20,2), -- 欠息户积数\n" +
                "  qyjjlx numeric(13,2), -- 欠息户应加减利息\n" +
                "  qxhlix numeric(25,8), -- 欠息户利息\n" +
                "  bwfxzh character varying(20), -- 表外复息户\n" +
                "  bwfxll numeric(9,7), -- 复息户利率\n" +
                "  bwfxye numeric(15,2), -- 复息户账户余额\n" +
                "  bwfxjs numeric(20,2), -- 复息户积数\n" +
                "  fxyjlx numeric(13,2), -- 复息户应加减利息\n" +
                "  bwfxlx numeric(25,8), -- 复息户利息\n" +
                "  sor_data_date date, -- 数据日期\n" +
                "  xcjxrq character varying(8), -- 结息日期\n" +
                "  manageuser character varying(20), -- 客户经理\n" +
                "  manageorg character varying(30) -- 客户经理所属机构\n" +
                ")";


        String sql4 = "CREATE TABLE plbs_app.plbs_app_ydhd_trade\n" +
                "(txn_time time without time zone\n" +
                ")";

        PGSQLStatementParser parser = new PGSQLStatementParser(sql4,
                new SQLParserFeature[] { SQLParserFeature.KeepComments });

        List<SQLStatement> stmtList = parser.parseStatementList();

        StringBuilder out = new StringBuilder();
        PGOutputVisitor visitor = new PGOutputVisitor(out, false);
        for (int i = 0; i < stmtList.size(); ++i) {
            stmtList.get(i).accept(visitor);
        }

        System.out.println(out.toString());
    }

}
