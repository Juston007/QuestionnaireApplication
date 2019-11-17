using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AnswerEntity
{
    public class UserEntity
    {

        public UserEntity(String uid, String pwd, String name, String className, String token, String tokenCreateTime, String regTime, String lastLoginTime,bool isComplete = false,bool locking = false,bool isAdmin = false)
        {
            this.uid = uid;
            this.pwd = pwd;
            this.name = name;
            this.className = className;
            this.token = token;
            this.tokenCreateTime = DateTime.Parse(tokenCreateTime == String.Empty ? "1970/01/01" : tokenCreateTime);
            this.regTime = DateTime.Parse(regTime == String.Empty ? "1970/01/01" : regTime);
            this.lastLoginTime = DateTime.Parse(regTime == String.Empty ? "1970/01/01" : lastLoginTime);
            this.locking = locking;
            this.isAdmin = isAdmin;
            this.isComplete = isComplete;
        }

        #region 字段

        //字段
        private String uid, pwd, token, name,className;
        private DateTime tokenCreateTime, regTime, lastLoginTime;
        private bool locking = false, isAdmin = false, isComplete = false;

        #endregion

        #region 属性
        public String Name
        {
            get { return name; }
        }

        private String Token
        {
            get { return token; }
        }

        private String Pwd
        {
            get { return pwd; }
        }

        public String Uid
        {
            get { return uid; }
        }

        public DateTime LastLoginTime
        {
            get { return lastLoginTime; }
        }

        public DateTime RegTime
        {
            get { return regTime; }
        }

        private DateTime TokenCreateTime
        {
            get { return tokenCreateTime; }
        }

        public String ClassName
        {
            get { return className; }
        }

        public bool Locking
        {
            get { return locking; }
        }

        public bool IsAdmin
        {
            get { return isAdmin; }
        }


        public bool IsComplete
        {
            get { return isComplete; }
        }

        #endregion

        #region 方法
        /// <summary>
        /// 验证密码
        /// </summary>
        /// <param name="pwd">密码</param>
        /// <returns>是否正确</returns>
        public bool authPwd(String pwd)
        {
            return pwd == this.Pwd;
        }

        /// <summary>
        /// 验证Token
        /// </summary>
        /// <param name="token">令牌</param>
        /// <returns>是否一致</returns>
        public bool authToekn(String token)
        {
            return token == Token;
        }

        /// <summary>
        /// 获取令牌生成时间
        /// </summary>
        /// <returns>生成时间</returns>
        public DateTime getTokenCreateTime()
        {
            return tokenCreateTime;
        }

        /// <summary>
        /// Token是否为空
        /// </summary>
        /// <returns></returns>
        public bool tokenIsNull()
        {
            return token == String.Empty;
        }

        /// <summary>
        /// 获取Token
        /// </summary>
        /// <param name="pwd">密码</param>
        /// <returns>Token 密码验证失败返回空字符</returns>
        public String getToken(String Pwd)
        {
            if (authPwd(Pwd))
                return Token;
            else
                return String.Empty;
        }

        #endregion

    }
}
