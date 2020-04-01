package com.yonyou.cases;

import com.yonyou.config.TestConfig;
import com.yonyou.model.InterfaceName;
import com.yonyou.model.LoginCase;
import com.yonyou.utils.ConfigFile;
import com.yonyou.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.mybatis.spring.SqlSessionTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginTest {



    @BeforeTest(groups = "login",description = "获取httpClient对象")
    public void beforeTest(){
        TestConfig.defaultHttpClient = new DefaultHttpClient();
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.getWithParamUrl = ConfigFile.getUrl(InterfaceName.GETWITHPARAM);

    }

    @Test(groups = "login",description = "用户登录接口")
    public void login() throws IOException {

        SqlSession session = DatabaseUtil.getSqlSession();
        //查询数据库中的用例数
        int count = session.selectOne("loginCount");
        //System.out.println(count);

        //循环数据库中用例
        for(int i = 1;i <= count;i++){
            //查询数据库中的请求值
            LoginCase loginCaseRequest = session.selectOne("loginCaseRequest",i);
            //调用方法得到接口返回值
            JSONArray resultJson = getJsonResult(loginCaseRequest);
            //查询数据库中的期望结果
            LoginCase loginCaseResponse = session.selectOne("loginCaseResponse",i);
            //将查询数据库中的期望结果存储进列表并转成json格式
            List expect = Arrays.asList(loginCaseResponse);
            JSONArray jsonArray = new JSONArray(expect);

            //输出期望与实际
            System.out.println("期望："+jsonArray.toString());
            System.out.println("实际："+resultJson.toString());
            //比较期望结果与实际结果
            Assert.assertEquals(resultJson.toString(),jsonArray.toString());
        }




    }



    private JSONArray getJsonResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        //将数据库中查到的参数传到请求中
        param.put("username",loginCase.getUsername());
        param.put("password",loginCase.getPassword());
        //设置请求头信息
        post.setHeader("content-type","application/json;charset=utf-8");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList = Arrays.asList(result);
        //System.out.println(resultList.toString());

        JSONArray array = new JSONArray(resultList.toString());
        //如果请求成功，返回status为01，将返回的cookies值保存
        if(array.getJSONObject(0).get("status").equals("01")){
            TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
            // System.out.println(array.getJSONObject(0).get("status"));
        }

        //System.out.println(array.toString());
        return array;
    }





}
