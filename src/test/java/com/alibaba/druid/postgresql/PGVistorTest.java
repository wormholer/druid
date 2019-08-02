package com.alibaba.druid.postgresql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import org.junit.Test;

import java.util.List;

/**
 * Created by wangcheng on 8/2/19.
 */
public class PGVistorTest {

    @Test
    public void testVisitor()
    {
        String sql = "CREATE TEMP TABLE\n" +
                "  VT_ACCT_CUST_FXQ_ORG( \n" +
                "     Account_Num            VARCHAR(200)           NOT NULL ----协议号\n" +
                "    ,Account_Modifier_Num   VARCHAR(100)           NOT NULL ----协议修饰符\n" +
                "    ,Open_Org\t\t\t\t\t\t\t\tCHAR(9)         \t\t\t\t\t\t\t\t----开户行号\n" +
                "    ,FXQ_ORG\t\t\t\t\t\t\t\tCHAR(9)         \t\t\t\t\t\t\t\t----反洗钱机构\n" +
                "    ,Open_Org_Districts\t\t\tCHAR(6)\t\t\t\t\t\t\t\t\t\t\t\t\t----开户机构行政区划\n" +
                "    ,BUSI_TYPE  \t\t\t\t\t\tCHAR(1)\t\t\t\t\t\t\t\t\t\t\t\t\t----业务标识\n" +
                "  )";
        PGSQLStatementParser parser = new PGSQLStatementParser(sql);

        List<SQLStatement> stmtList = parser.parseStatementList();

        StringBuilder out = new StringBuilder();
        PGOutputVisitor visitor = new PGOutputVisitor(out, false);
        for (int i = 0; i < stmtList.size(); ++i) {
            stmtList.get(i).accept(visitor);
        }

        System.out.println(out.toString());
    }

}
