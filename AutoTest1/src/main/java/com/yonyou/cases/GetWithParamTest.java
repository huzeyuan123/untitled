package com.yonyou.cases;

import com.yonyou.config.TestConfig;
import com.yonyou.model.GetWithParamCase;
import com.yonyou.utils.DatabaseUtil;
import com.yonyou.utils.Assertion;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Listeners({com.yonyou.config.AssertionListener.class})
public class GetWithParamTest {

    @Test(groups = "getWithParam",description = "需要参数的get请求")
    public void getWithParam() throws IOException {

        SqlSession session = DatabaseUtil.getSqlSession();
        //查询数据库中的用例数
        int count = session.selectOne("getWithParamCount");
        System.out.println(count);
        //循环数据库中用例
        for(int i = 1;i <= count;i++){
            //查询数据库中的请求值
            GetWithParamCase getWithParamCaseRequest = session.selectOne("getWithParamCaseRequest",i);
            //调用方法得到接口返回值
            JSONArray resultJson = getResult(getWithParamCaseRequest);
            //查询数据库中的期望结果
            GetWithParamCase getWithParamCaseResponse = session.selectOne("getWithParamCaseResponse",i);
            //将查询数据库中的期望结果存储进列表并转成json格式
            List expect = Arrays.asList(getWithParamCaseResponse);
            JSONArray jsonArray = new JSONArray(expect);

            //输出期望与实际
            System.out.println("第" + i + "条用例：");
//            System.out.println("期望："+jsonArray.toString());
//            System.out.println("实际："+resultJson.toString());
            //比较期望结果与实际结果
            //Assert.assertEquals(resultJson.toString(),jsonArray.toString());
            Assertion.verifyEquals("第" + i + "条用例：" + resultJson.toString(),"第" + i + "条用例：" + jsonArray.toString());

        }

    }

    private JSONArray getResult(GetWithParamCase getWithParamCase) throws IOException {
        //创建httpGet请求
        HttpGet get = new HttpGet(TestConfig.getWithParamUrl + "?name=" + getWithParamCase.getName());

        //添加请求头信息
        get.setHeader("content-type","application/json;charset=utf-8");
        //添加cookies信息
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行get请求
        HttpResponse response = TestConfig.defaultHttpClient.execute(get);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList = Arrays.asList(result);
        JSONArray array = new JSONArray(resultList.toString());
        //System.out.println(array.getJSONObject(0).get("result"));



        return array;
    }


}
