using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using AnswerEntity;
using HelpUtil;
using DBHelp;
using Newtonsoft.Json;
using System.Collections;


namespace AnswerServices
{
    /// <summary>
    /// AnswerWebService 的摘要说明
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消注释以下行。 
    // [System.Web.Script.Services.ScriptService]
    public class AnswerWebService : System.Web.Services.WebService
    {
        public AnswerWebService()
        {
            //初始化数据库链接字符串
            ConfigUtil.ConnectionStr = System.Configuration.ConfigurationManager.AppSettings["ConStr"].ToString();
        }

        #region 账号相关

        [WebMethod(Description = "注册用户")]
        public void regUser(String uid,String pwd,String name,String className,bool isAdmin)
        {
            try
            {
                bool issuccess = DBUtil.addUser(uid, pwd, name, className, isAdmin);
                Util.writeWeb(Context, Status.Success, issuccess ? Status.Success : Status.Fail, issuccess ? "注册成功" : "注册失败,请稍后再试", uid);
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "修改密码")]
        public void modifyPwd(String uid,String oldPwd,String newPwd)
        {
            try
            {
                bool result = false;
                UserEntity entity = null;
                if (Util.checkUserStatus(uid, out entity))
                {
                    result = false;
                }
                else
                {
                    result = DBUtil.changePwd(uid, oldPwd, newPwd);
                }
                Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "修改成功" : "修改失败", String.Empty);
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "登入")]
        public void login(String uid, String pwd)
        {
            String result = String.Empty;
            bool issuccess = false;
            try
            {
                UserEntity entity = null;
                if (Util.checkUserStatus(uid, out entity))
                {
                    issuccess = false;
                }
                else
                {
                    result = DBUtil.login(uid, pwd);
                    issuccess = result != String.Empty;
                }
                Util.writeWeb(Context, Status.Success, issuccess ? Status.Success : Status.Fail, issuccess ? "登入成功" : "账户名称或密码不正确(亦或者您的账号已被锁定！)", result);
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        #endregion

        #region 资料相关
        [WebMethod(Description = "获取用户资料")]
        public void getUserInfo(String token,String queryUid)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    //检查权限
                    if (Util.checkPermission(loginUid, queryUid))
                    {
                        UserEntity model = DBUtil.getUserInfo(queryUid);
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取账户资料成功" : "没有找到此账户", datastr);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "更新用户资料")]
        public void updateUserInfo(String token, String updateUid, String name, int classID)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    if (Util.checkPermission(loginUid, updateUid))
                    {
                        result = DBUtil.updateUserInfo(updateUid, name, classID);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "更新资料成功" : "更新资料失败", String.Empty);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "获取班级信息")]
        public void getClassInfo(String token)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    if (Util.checkPermission(loginUid, String.Empty))
                    {
                        ArrayList model = DBUtil.getClass();
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取班级信息成功" : "没有班级信息", datastr);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }
        #endregion

        #region 报表相关

        [WebMethod(Description = "获取用户成绩记录")]
        public void getUserGradeRecord(String token,String queryUid)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    //检查权限
                    if (Util.checkPermission(loginUid, queryUid))
                    {
                        ArrayList model = DBUtil.getUserGradeRecord(queryUid);
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取用户历史成绩成功" : "查询不到数据", datastr);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "获取某班成绩排名")]
        public void getClassGradeRank(String token,String className)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    if (DBUtil.getUserInfo(loginUid).ClassName == className)
                    {
                        ArrayList model = DBUtil.getClassGradeRank(className);
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取班级排名成功" : "查询不到数据", datastr);
                    }
                    else
                    {
                        //检查权限
                        if (Util.checkPermission(loginUid, String.Empty))
                        {
                            ArrayList model = DBUtil.getClassGradeRank(className);
                            result = model != null;
                            String datastr = String.Empty;
                            if (result)
                                datastr = JsonConvert.SerializeObject(model);
                            Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取班级排名成功！" : "获取班级排名失败！", datastr);
                        }
                        else
                        {
                            Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                        }
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "获取班级完成情况")]
        public void getClassCompletion(String token,String className)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    //检查权限 是不是自己班，如果不是，那么无效
                    UserEntity entity = DBUtil.getUserInfo(loginUid);
                    if (entity.IsAdmin || entity.ClassName.ToString().Trim() == className)
                    {
                        ArrayList model = DBUtil.getClassCompletion(className);
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "获取班级完成情况成功" : "查询不到数据", datastr);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您没有权限！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        #endregion

        #region 题目相关

        [WebMethod(Description = "开始答题")]
        public void startAnswer(String token)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    //检查权限
                    UserEntity entity = null;
                    if (!Util.checkUserStatus(loginUid, out entity))
                    {
                        int id = 0;
                        int count = int.Parse(System.Configuration.ConfigurationManager.AppSettings["QuestionCount"].ToString());
                        ArrayList model = DBUtil.startAnswer(loginUid, count, out id);
                        result = model != null;
                        String datastr = String.Empty;
                        if (result)
                            datastr = JsonConvert.SerializeObject(model);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? String.Format("{0}", id) : "题库没有题目！", datastr);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您的账号已经被锁定！", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "结束答题")]
        public void submitAnswer(String token, int id, int grade)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    UserEntity entity = null;
                    if (!Util.checkUserStatus(loginUid, out entity))
                    {
                        result = DBUtil.submitAnswer(loginUid, id, grade);
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "上传成绩成功" : "上传成绩失败", String.Empty);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您的账号已经被锁定", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        [WebMethod(Description = "上传题目")]
        public void uploadQuestion(String token, String content, String option1, String option2, String option3, String option4, int passoption, uint score)
        {
            try
            {
                String loginUid = DBUtil.getUidByToken(token);
                bool result = loginUid != String.Empty;
                if (result)
                {
                    UserEntity entity = null;
                    if (!Util.checkUserStatus(loginUid, out entity))
                    {
                        if (entity.IsAdmin)
                        {
                            result = DBUtil.uploadQuestion(new QuestionEntity(1, content, option1, option2, option3, option4, (sbyte)passoption, (int)score));
                        }
                        else
                        {
                            result = false;
                        }
                        Util.writeWeb(Context, Status.Success, result ? Status.Success : Status.Fail, result ? "上传题目成功" : "上传题目失败", String.Empty);
                    }
                    else
                    {
                        Util.writeWeb(Context, Status.Success, Status.Fail, "您的账号已经被锁定", String.Empty);
                    }
                }
                else
                {
                    Util.writeWeb(Context, Status.Success, Status.Fail, "请求非法", String.Empty);
                }
            }
            catch (Exception ex)
            {
                Util.writeError(Context, ex.Message);
            }
        }

        #endregion
    }

    public class Util
    {
        public static void writeWeb(HttpContext context, Status status, Status statuscode, String msg, String data, String errormsg = null)
        {
            //设置ContentType
            context.Response.ContentType = "application/json";
            //获取序列化字符串
            String result = WebUtil.serializeResponse(status, statuscode, msg, data, errormsg);
            //写入
            context.Response.Write(result);
            //将所有缓冲写入至客户端
            context.Response.Flush();
        }

        public static void writeError(HttpContext context, String errormsg)
        {
            Util.writeWeb(context, Status.Exception, Status.Fail, String.Empty, String.Empty, "参考原因：" + errormsg);
        }

        public static bool checkPermission(String loginUid, String operationUid)
        {
            if (loginUid == operationUid)
            {
                return true;
            }
            else
            {
                UserEntity loginUserEntity = null;
                if (checkUserStatus(loginUid, out loginUserEntity))
                    return false;
                if (loginUserEntity == null)
                    return false;
                return loginUserEntity.IsAdmin;
            }
        }

        public static bool checkUserStatus(String loginUid, out UserEntity entity)
        {
            entity = DBUtil.getUserInfo(loginUid);
            if (entity != null)
                return entity.Locking;
            else
                return false;
        }
    }
}
