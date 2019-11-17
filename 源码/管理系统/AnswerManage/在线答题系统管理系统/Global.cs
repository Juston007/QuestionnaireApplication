using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace 在线答题系统管理系统
{
    //存储一些全局信息
    class Global
    {
        //访问令牌
        public static String token, serverUrl;
        //服务器地址
        public static String loginInfoPath = "Setting.txt";


        public static String loginMethod = "login";
        public static String getUserInfoMethod = "getUserInfo";
        public static String uploadQuestionMethod = "uploadQuestion";
        public static String regUserMethod = "regUser";
    }
}
