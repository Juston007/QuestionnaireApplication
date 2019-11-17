using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AnswerEntity;

namespace DBHelp
{
    public class DBUtil
    {
        /// <summary>
        /// 线程锁对象
        /// </summary>
        private static object obj = new object();

        #region 账号

        /// <summary>
        /// 注册用户
        /// </summary>
        /// <param name="uid">ID</param>
        /// <param name="pwd">密码</param>
        /// <param name="alias">别名</param>
        /// <returns>注册结果</returns>
        public static bool addUser(String uid, String pwd, String name, String className, bool isAdmin)
        {
            if (existUser(uid))
            {
                return false;
            }
            //查询班级信息 如果不存在这个班级 那么创建一个班级
            int classId = queryClassID(className);
            //创建失败直接返回-1
            if (classId == -1)
                return false;
            //向数据库插入新用户信息
            Object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@uid", uid), new SqlParameter("@pwd", pwd), new SqlParameter("@name", name), new SqlParameter("@class", classId), new SqlParameter("@regtime", DateTime.Now.ToString()), new SqlParameter("isadmin", isAdmin) });
            int result = SQLUtil.excuteSQL("INSERT INTO T_UserInfo ([Uid],Pwd,Name,Class,RegTime,Locking,isAdmin,isComplete) VALUES(@uid,@pwd,@name,@class,@regtime,0,@isadmin,0)", parameter);
            return result > 0;
        }


        /// <summary>
        /// 登入
        /// </summary>
        /// <param name="uid">ID</param>
        /// <param name="pwd">密码</param>
        /// <returns>访问令牌</returns>
        public static String login(String uid, String pwd)
        {
            //获取用户实体
            UserEntity user = getUserInfo(uid);
            //当不为空就是有此账户 否则无此账户
            if (user != null)
            {
                //验证密码
                if (!user.authPwd(pwd))
                {
                    return String.Empty;
                }
                //令牌
                String token = String.Empty;
                //更新最后登入时间
                object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@time", DateTime.Now.ToString()), new SqlParameter("@uid", uid) });
                SQLUtil.excuteSQL("UPDATE T_UserInfo SET LastLoginTime = @time WHERE T_UserInfo.Uid = @uid", parameter);
                //生成Token 如果Token生成时间距离上次生成大于两天 那么重新生成
                if (user.tokenIsNull())
                {
                    String str = generateToken(uid);
                    return str;
                }
                else
                {
                    if ((DateTime.Now - user.getTokenCreateTime()).TotalDays > 2)
                    {
                        return generateToken(uid);
                    }
                    return user.getToken(pwd);
                }
            }
            else
                return String.Empty;
        }

        /// <summary>
        /// 修改密码
        /// </summary>
        /// <param name="uid">ID</param>
        /// <param name="pwd">密码</param>
        /// <returns>执行结果</returns>
        public static bool changePwd(String uid, String oldpwd, String newpwd)
        {
            if (!existUser(uid) || oldpwd == newpwd)
                return false;
            Object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@newpwd", newpwd), new SqlParameter("@oldpwd", oldpwd), new SqlParameter("@uid", uid) });
            int result = SQLUtil.excuteSQL("Update T_UserInfo Set Pwd = @newpwd ,Token = NULL , TokenCreateTime = NULL Where [Uid] = @uid And Pwd = @oldpwd", parameter);
            return result > 0;
        }

        #endregion

        #region 资料相关

        /// <summary>
        /// 更新个人资料
        /// </summary>
        /// <param name="uid">ID</param>
        /// <param name="name">名字</param>
        /// <param name="classID">班级名称</param>
        /// <returns>执行结果</returns>
        public static bool updateUserInfo(String uid, String name, int classID)
        {
            int result = SQLUtil.excuteSQL("UPDATE T_UserInfo SET Name = @name,Class = @classID Where Uid = @uid", getParameterList(new SqlParameter[] { new SqlParameter("@name", name), new SqlParameter("@classID", classID), new SqlParameter("@uid", uid) }));
            return result > 0;
        }

        /// <summary>
        /// 获取用户信息
        /// </summary>
        /// <param name="uid">ID</param>
        /// <returns>用户模型实例 获取不到返回null</returns>
        public static UserEntity getUserInfo(String uid)
        {
            if (!existUser(uid))
                return null;
            Object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@uid", uid) });
            ArrayList arr = SQLUtil.rawQuery(@"SELECT  T_UserInfo.isComplete, T_UserInfo.isAdmin, T_UserInfo.Uid, T_UserInfo.Pwd, T_UserInfo.Name, T_Class.ClassName, T_UserInfo.Token, T_UserInfo.TokenCreateTime, T_UserInfo.RegTime, 
                      T_UserInfo.LastLoginTime, T_UserInfo.Locking
FROM         T_UserInfo INNER JOIN
                      T_Class ON T_UserInfo.Class = T_Class.ClassID WHERE T_UserInfo.Uid = @uid", parameter);
            if (arr.Count > 0)
            {
                Hashtable table = (Hashtable)arr[0];
                //密码
                String pwd = table["Pwd"].ToString().Trim();
                //姓名
                String name = table["Name"].ToString().Trim();
                //班级名称
                String className = table["ClassName"].ToString().Trim();
                //Token
                String token = String.Empty;
                if (table["Token"] != null)
                    token = table["Token"].ToString().Trim();
                //Token注册时间
                String generatestr = "1970/01/01 8:00";
                if (table["TokenCreateTime"] != null)
                {
                    generatestr = table["TokenCreateTime"].ToString().Trim();
                    if (generatestr == String.Empty)
                    {
                        generatestr = "1970/01/01 8:00";
                    }
                }
                //注册时间
                String regtime = table["RegTime"].ToString().Trim();
                //最后登录时间
                String lastLoginTime = "1970/01/01 8:00";
                if (table["LastLoginTime"] != null)
                {
                    lastLoginTime = table["LastLoginTime"].ToString().Trim();
                    if (lastLoginTime == String.Empty)
                    {
                        lastLoginTime = "1970/01/01 8:00";
                    }
                }
                //是否锁定
                String lockingStr = table["Locking"].ToString().Trim();
                bool locking = Convert.ToBoolean(lockingStr == "" ? "True" : lockingStr);
                //是否管理员
                String isAdminStr = table["isAdmin"].ToString().Trim();
                bool isAdmin = Convert.ToBoolean(isAdminStr == "" ? "True" : isAdminStr);
                //是否完成
                String isCompleteStr = table["isComplete"].ToString().Trim();
                bool isComplete = Convert.ToBoolean(isCompleteStr == "" ? "True" : isCompleteStr);
                //UserEntivy对象
                UserEntity user = new UserEntity(uid, pwd, name, className, token, generatestr, regtime, lastLoginTime, isComplete, locking, isAdmin);
                return user;
            }
            else
                return null;
        }

        /// <summary>
        /// 获取班级
        /// </summary>
        /// <returns>班级</returns>
        public static ArrayList getClass()
        {
            ArrayList arr = SQLUtil.rawQuery("SELECT * FROM T_Class", null);
            if (arr != null)
            {
                if (arr.Count == 0)
                    return null;
                ArrayList list = new ArrayList();
                for (int i = 0; i < arr.Count; i++)
                {
                    Hashtable table = (Hashtable)arr[i];
                    int id = int.Parse(table["ClassID"].ToString().Trim());
                    String name = table["ClassName"].ToString().Trim();
                    list.Add(new ClassEntity(id, name));
                }
                return list;
            }
            else
                return null;
        }
        #endregion

        #region 报表
        /// <summary>
        /// 获取某个用户的历史成绩
        /// </summary>
        /// <param name="uid">ID</param>
        /// <returns>成绩集合</returns>
        public static ArrayList getUserGradeRecord(String uid)
        {
            if (existUser(uid))
            {
                //查询历史
                ArrayList queryData = SQLUtil.rawQuery(@"SELECT  T_AnswerRecord.R_ID, T_AnswerRecord.R_Grage, T_AnswerRecord.R_StartTime, T_AnswerRecord.R_SubmitTime, T_UserInfo.Name, T_AnswerRecord.R_Uid
FROM         T_AnswerRecord INNER JOIN
                      T_UserInfo ON T_AnswerRecord.R_Uid = T_UserInfo.Uid WHERE T_UserInfo.Uid = @uid", new SqlParameter[] { new SqlParameter("uid", uid) });
                if (queryData == null)
                {
                    return null;
                }
                else
                {
                    if (queryData.Count == 0)
                    {
                        return null;
                    }
                    else
                    {
                        ArrayList recordArr = new ArrayList();
                        for (int i = 0; i < queryData.Count; i++)
                        {
                            Hashtable table = (Hashtable)queryData[i];
                            int id = int.Parse(table["R_ID"].ToString().Trim());
                            String name = table["Name"].ToString().Trim();
                            int grade = int.Parse(table["R_Grage"].ToString().Trim());
                            String startTime = table["R_StartTime"].ToString().Trim();
                            String submitTime = table["R_SubmitTime"].ToString().Trim();
                            recordArr.Add(new AnswerRecordEntity(id, grade, name, startTime, submitTime));
                        }
                        return recordArr;
                    }
                }
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 获取班级排名
        /// </summary>
        /// <param name="className">班级名称</param>
        /// <returns></returns>
        public static ArrayList getClassGradeRank(String className)
        {
            //查询历史
            ArrayList queryData = SQLUtil.rawQuery(@"SELECT T_AnswerRecord.R_Uid ,T_AnswerRecord.R_SubmitTime, T_AnswerRecord.R_StartTime, T_AnswerRecord.R_Grage, T_AnswerRecord.R_ID, T_UserInfo.Name FROM         
T_AnswerRecord INNER JOIN
                      T_UserInfo ON T_AnswerRecord.R_Uid = T_UserInfo.Uid
                      WHERE T_AnswerRecord.R_Uid IN 
                      (SELECT T_UserInfo.Uid FROM T_UserInfo WHERE T_UserInfo.Class IN
                      (SELECT T_Class.ClassID FROM T_Class WHERE ClassName = @className))", new SqlParameter[] { new SqlParameter("className", className) });
            if (queryData == null)
            {
                return null;
            }
            else
            {
                if (queryData.Count == 0)
                {
                    return null;
                }
                else
                {
                    ArrayList recordArr = new ArrayList();
                    for (int i = 0; i < queryData.Count; i++)
                    {
                        Hashtable table = (Hashtable)queryData[i];
                        int id = int.Parse(table["R_ID"].ToString().Trim());
                        String name = table["Name"].ToString().Trim();
                        int grade = int.Parse(table["R_Grage"].ToString().Trim());
                        String startTime = table["R_StartTime"].ToString().Trim();
                        String submitTime = table["R_SubmitTime"].ToString().Trim();
                        String uid = table["R_Uid"].ToString().Trim();
                        recordArr.Add(new AnswerRecordEntity(id, grade, name, startTime, submitTime) { Uid = uid });
                    }
                    //真实的结果
                    ArrayList list = new ArrayList();
                    //一直处理第0条数据
                    while (recordArr.Count != 0)
                    {
                        //实例1
                        AnswerRecordEntity recordEntity1 = (AnswerRecordEntity)recordArr[0];
                        //遍历除了序号0的所有实例 如果uid相同 那么比较成绩 成绩高的赋给实例1 然后在记录表中删除
                        for (int i = 1; i < recordArr.Count; i++)
                        {
                            AnswerRecordEntity recordEntity2 = (AnswerRecordEntity)recordArr[i];
                            if (recordEntity1.Uid == recordEntity2.Uid)
                            {
                                if (recordEntity2.Grade > recordEntity1.Grade)
                                {
                                    recordEntity1 = recordEntity2;
                                }
                                recordArr.RemoveAt(i);
                                i--;
                            }
                        }
                        //删除第0条数据
                        recordArr.RemoveAt(0);
                        //将实例1添加到结果数据集中
                        list.Add(recordEntity1);
                    }
                    //排序
                    for (int i = 0; i < list.Count; i++)
                    {
                        for (int j = i + 1; j < list.Count; j++)
                        {
                            AnswerRecordEntity recordj = (AnswerRecordEntity)list[j];
                            AnswerRecordEntity recordi = (AnswerRecordEntity)list[i];
                            if (recordj.Grade > recordi.Grade)
                            {
                                list[j] = list[i];
                                list[i] = recordj;
                            }
                        }
                    }
                    return list;
                }
            }
        }

        /// <summary>
        /// 获取班级完成情况
        /// </summary>
        /// <param name="className">班级名称</param>
        /// <returns>完成情况</returns>
        public static ArrayList getClassCompletion(String className)
        {
            ArrayList arr = SQLUtil.rawQuery("SELECT Uid,Name,isComplete FROM T_UserInfo WHERE T_UserInfo.Class IN (SELECT T_Class.ClassID FROM T_Class WHERE T_Class.ClassName = @className)", new SqlParameter[] { new SqlParameter("className", className) });
            if (arr != null)
            {
                if (arr.Count == 0)
                    return null;
                ArrayList list = new ArrayList();
                for (int i = 0; i < arr.Count; i++)
                {
                    Hashtable table = (Hashtable)arr[i];
                    String uid = table["Uid"].ToString().Trim();
                    String name = table["Name"].ToString().Trim();
                    bool isComplete = bool.Parse(table["isComplete"].ToString().Trim());
                    list.Add(new UserEntity(uid, null, name, className, String.Empty, String.Empty, String.Empty, String.Empty, isComplete));
                }
                return list;
            }
            else
                return null;
        }

        #endregion

        #region 题目相关

        public static ArrayList startAnswer(String uid, int questionCount, out int Q_ID)
        {
            Q_ID = -1;
            if (!existUser(uid))
                return null;
            //注册记录
            Q_ID = createRecordID(uid);
            //获取随机50个题目
            int[] qID = new int[50];
            Random random = new Random(int.Parse(DateTime.Now.ToString("HHmmssfff")) + Q_ID);
            for (int i = 0; i < qID.Length; i++)
            {
            tag:
                int randomNumber = random.Next(questionCount);
                if (qID.Contains(randomNumber))
                    goto tag;
                else
                    qID[i] = randomNumber;
            }
            StringBuilder strBuilder = new StringBuilder("SELECT *  FROM T_QuestionsInfo WHERE Q_ID = '" + qID[0] + "'");
            for (int i = 1; i < qID.Length; i++)
            {
                strBuilder.Append("OR Q_ID = '" + qID[i] + "'");
            }
            ArrayList list = SQLUtil.rawQuery(strBuilder.ToString(), null);
            if (list == null)
                return null;
            ArrayList result = new ArrayList();
            for (int i = 0; i < list.Count; i++)
            {
                Hashtable table = (Hashtable)list[i];
                long id = long.Parse(table["Q_ID"].ToString().Trim());
                String content = table["Q_Content"].ToString().Trim();
                String option1 = table["Q_Option1"].ToString().Trim();
                String option2 = table["Q_Option2"].ToString().Trim();
                String option3 = table["Q_Option3"].ToString().Trim();
                String option4 = table["Q_Option4"].ToString().Trim();
                sbyte passOption = sbyte.Parse(table["Q_PassOption"].ToString().Trim());
                int score = int.Parse(table["Q_Score"].ToString().Trim());
                QuestionEntity question = new QuestionEntity(id, content, option1, option2, option3, option4, passOption, score);
                result.Add(question);
            }
            //打乱排序
            return ListRandom(result); ;
        }

        public static int createRecordID(String uid)
        {
            lock (obj)
            {
                //注册记录
                SQLUtil.excuteSQL("INSERT INTO T_AnswerRecord (R_Uid,R_Grage,R_StartTime) VALUES(@uid,@grade,@time)", new SqlParameter[] { new SqlParameter("uid", uid), new SqlParameter("grade", "0"), new SqlParameter("time", DateTime.Now.ToString()) });
                //查询ID
                ArrayList arr = SQLUtil.rawQuery("SELECT TOP 1 R_ID FROM T_AnswerRecord ORDER BY R_ID DESC", null);
                Hashtable table = (Hashtable)arr[0];
                return int.Parse(table["R_ID"].ToString().Trim());
            }
        }

        /// <summary>
        /// 上传成绩
        /// </summary>
        /// <param name="uid">用户UID</param>
        /// <param name="id">记录ID</param>
        /// <param name="grade">成绩</param>
        /// <returns>上传结果</returns>
        public static bool submitAnswer(String uid, int id, int grade)
        {
            DateTime dt = DateTime.Now;
            DateTime dt2 = dt.AddMinutes(-40);
            int result = SQLUtil.excuteSQL("UPDATE T_AnswerRecord SET R_Grage = @grade,R_SubmitTime = @time1 WHERE R_ID = @id AND R_Uid = @uid AND R_SubmitTime IS NULL AND R_StartTime >= @time2", new SqlParameter[] { new SqlParameter("uid", uid), new SqlParameter("id", id), new SqlParameter("grade", grade), new SqlParameter("time1", dt), new SqlParameter("time2", dt2) });
            if (result > 0)
            {
                //更新完成状态
                result = SQLUtil.excuteSQL("UPDATE T_UserInfo SET isComplete = 1 WHERE Uid = @uid", new SqlParameter[] { new SqlParameter("@uid", uid) });
                return true;
            }
            else
                return false;
        }

        /// <summary>
        /// 上传题目
        /// </summary>
        /// <param name="questionEntity">题目实体</param>
        /// <returns>上传题目</returns>
        public static bool uploadQuestion(QuestionEntity questionEntity)
        {
            if (questionEntity == null)
                return false;
            ArrayList list = SQLUtil.rawQuery("SELECT TOP 1 Q_ID FROM T_QuestionsInfo ORDER BY Q_ID DESC", null);
            int startID = 0;
            if (list.Count != 0)
            {
                startID = int.Parse(((Hashtable)list[0])["Q_ID"].ToString().Trim());
            }
            //ID往后推一位
            startID++;
            SqlParameter[] paramter = new SqlParameter[] { new SqlParameter("id", startID), new SqlParameter("content", questionEntity.Content), new SqlParameter("option1", questionEntity.Option1), new SqlParameter("option2", questionEntity.Option2), new SqlParameter("option3", questionEntity.Option3), new SqlParameter("option4", questionEntity.Option4), new SqlParameter("passoption", (int)questionEntity.PassOption), new SqlParameter("score", questionEntity.Score) };
            startID = SQLUtil.excuteSQL("INSERT INTO T_QuestionsInfo VALUES (@id,@content,@option1,@option2,@option3,@option4,@passoption,@score);", paramter);
            return startID > 0;
        }

        #endregion

        #region 工具

        /// <summary>
        /// 随机排列数组元素
        /// </summary>
        /// <param name="myList"></param>
        /// <returns></returns>
        private static ArrayList ListRandom(ArrayList myList)
        {

            Random ran = new Random();
            ArrayList newList = new ArrayList();
            int index = 0;
            Object temp = 0;
            for (int i = 0; i < myList.Count; i++)
            {

                index = ran.Next(0, myList.Count - 1);
                if (index != i)
                {
                    temp = myList[i];
                    myList[i] = myList[index];
                    myList[index] = temp;
                }
            }
            return myList;
        }

        /// <summary>
        /// 获取参数列表
        /// </summary>
        /// <param name="parameter"></param>
        /// <returns></returns>
        private static Object[] getParameterList(SqlParameter[] parameter)
        {
            return parameter;
        }

        /// <summary>
        /// 根据Token获取Uid
        /// </summary>
        /// <param name="token">令牌</param>
        /// <returns>Uid</returns>
        public static String getUidByToken(String token)
        {
            Object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@token", token) });
            ArrayList arr = SQLUtil.rawQuery("SELECT [Uid] FROM T_UserInfo WHERE Token = @token", parameter);
            if (arr.Count > 0)
            {
                Hashtable table = (Hashtable)arr[0];
                return table["Uid"].ToString().Trim();
            }
            else
            {
                return String.Empty;
            }
        }

        /// <summary>
        /// 检查账户是否存在
        /// </summary>
        /// <param name="uid">ID</param>
        /// <returns>账户是否存在</returns>
        public static bool existUser(String uid)
        {
            Object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@uid", uid) });
            ArrayList arr = SQLUtil.rawQuery("SELECT 1 FROM T_UserInfo WHERE [Uid] = @uid", parameter);
            return arr.Count > 0;
        }

        /// <summary>
        /// 生成令牌
        /// </summary>
        /// <param name="uid">ID</param>
        /// <returns>令牌</returns>
        private static String generateToken(String uid)
        {
            //生成Token
            String str = DateTime.Now.Millisecond + new Random().NextDouble() + uid;
            str = Convert.ToBase64String(System.Text.Encoding.Unicode.GetBytes(str));
            //上传至数据库
            object[] parameter = getParameterList(new SqlParameter[] { new SqlParameter("@token", str), new SqlParameter("@time", DateTime.Now.ToString()), new SqlParameter("@uid", uid) });
            SQLUtil.excuteSQL("UPDATE T_UserInfo SET Token = @token,TokenCreateTime = @time WHERE T_UserInfo.Uid = @uid", parameter);
            return str;
        }

        /// <summary>
        /// 根据班级名称查询班级ID
        /// </summary>
        /// <param name="name">班级名称</param>
        /// <returns>班级ID</returns>
        private static int queryClassNameById(String name)
        {
            ArrayList list = SQLUtil.rawQuery("SELECT T_Class.ClassID FROM T_Class WHERE ClassName = @name", new SqlParameter[] { new SqlParameter("name", name) });
            if (list.Count == 0)
                return -1;
            Hashtable table = (Hashtable)list[0];
            int id = -1;
            try
            {
                id = int.Parse(table["ClassID"].ToString().Trim());
            }
            catch { }
            return id;
        }

        /// <summary>
        /// 查询班级ID
        /// </summary>
        /// <param name="name">班级名称</param>
        /// <returns>班级ID</returns>
        private static int queryClassID(String name)
        {
            //查询班级是否存在
            int id = queryClassNameById(name);
            if (id == -1)
            {
                //创建班级
                id = SQLUtil.excuteSQL("INSERT INTO T_Class VALUES(@name);", new SqlParameter[] { new SqlParameter("name", name) });
                if (id < 0)
                {
                    return -1;
                }
                else
                {
                    return queryClassNameById(name);
                }
            }
            else
                return id;
        }

        #endregion
    }
}
