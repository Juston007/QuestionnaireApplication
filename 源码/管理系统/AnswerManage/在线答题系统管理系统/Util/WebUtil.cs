using AnswerEntity;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace 在线答题系统管理系统
{
    class WebUtil
    {
        public static String postRequestData(string url, IDictionary<string, string> parameters)
        {
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;//创建请求对象
            request.Method = "POST";//请求方式
            request.ContentType = "application/x-www-form-urlencoded";//链接类型
            //构造查询字符串
            if (!(parameters == null || parameters.Count == 0))
            {
                StringBuilder buffer = new StringBuilder();
                bool first = true;
                foreach (string key in parameters.Keys)
                {
                    if (!first)
                    {
                        buffer.AppendFormat("&{0}={1}", key, parameters[key]);
                    }
                    else
                    {
                        buffer.AppendFormat("{0}={1}", key, parameters[key]);
                        first = false;
                    }
                }
                byte[] data = Encoding.UTF8.GetBytes(buffer.ToString());
                //写入请求流
                using (Stream stream = request.GetRequestStream())
                {
                    stream.Write(data, 0, data.Length);
                }
            }
            WebResponse response = request.GetResponse();
            StreamReader reader = new StreamReader(response.GetResponseStream());
            return reader.ReadToEnd();
        }

        public static bool getToken(String uid, String pwd, out String token)
        {
            Dictionary<String, String> param = new Dictionary<string, string>();
            param.Add("uid", uid);
            param.Add("pwd", pwd);
            String resultStr = postRequestData(Global.serverUrl + '/' + Global.loginMethod, param);
            ResponseModel model = JsonConvert.DeserializeObject<ResponseModel>(resultStr);

            if (model.Status == Status.Success)
            {
                if (model.StatusCode == Status.Success)
                {
                    token = model.Data.ToString().Trim();
                    return true;
                }
                else
                {
                    token = model.Messgae;
                    return false;
                }
            }
            else
            {
                token = model.ErrorMsg;
                return false;
            }
        }

        /// <summary>
        /// 查询用户信息
        /// </summary>
        /// <param name="token">访问令牌</param>
        /// <param name="queryUid">查询的UID</param>
        /// <returns>用户实体</returns>
        public static UserEntity getUserInfo(String token, String queryUid)
        {
            Dictionary<String, String> param = new Dictionary<string, string>();
            param.Add("token", token);
            param.Add("queryUid", queryUid);
            String resultStr = postRequestData(Global.serverUrl + '/' + Global.getUserInfoMethod, param);
            ResponseModel model = JsonConvert.DeserializeObject<ResponseModel>(resultStr);
            if (model.Status == Status.Success)
            {
                if (model.StatusCode == Status.Success)
                {
                    var entity = JsonConvert.DeserializeObject<UserEntity>(model.Data);
                    return entity;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 上传题目
        /// </summary>
        /// <param name="token">访问令牌</param>
        /// <param name="entity">题目实体</param>
        /// <returns>上传结果</returns>
        public static bool uploadQuestion(String token, QuestionEntity entity)
        {
            Dictionary<String, String> param = new Dictionary<string, string>();
            param.Add("token", token);
            param.Add("content", entity.content);
            param.Add("option1", entity.option1);
            param.Add("option2", entity.option2);
            param.Add("option3", entity.option3);
            param.Add("option4", entity.option4);
            param.Add("passoption", entity.passOption.ToString());
            param.Add("score", entity.score.ToString());
            String resultStr = postRequestData(Global.serverUrl + '/' + Global.uploadQuestionMethod, param);
            ResponseModel model = JsonConvert.DeserializeObject<ResponseModel>(resultStr);
            if (model.Status == Status.Success)
            {
                return model.StatusCode == Status.Success;
            }
            else
            {
                return false;
            }
        }

        public static bool regUser(UserEntity entity)
        {
            Dictionary<String, String> param = new Dictionary<string, string>();
            param.Add("uid", entity.uid);
            param.Add("pwd", entity.pwd);
            param.Add("name", entity.name);
            param.Add("className", entity.className);
            param.Add("isAdmin", entity.isAdmin.ToString());
            String resultStr = postRequestData(Global.serverUrl + '/' + Global.regUserMethod, param);
            ResponseModel model = JsonConvert.DeserializeObject<ResponseModel>(resultStr);
            if (model.Status == Status.Success)
            {
                return model.StatusCode == Status.Success;
            }
            else
            {
                return false;
            }
        }
    }
}
