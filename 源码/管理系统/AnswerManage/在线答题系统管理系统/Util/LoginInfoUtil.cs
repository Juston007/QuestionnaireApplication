using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using AnswerEntity;
using Newtonsoft.Json;

namespace 在线答题系统管理系统
{
    class LoginInfoUtil
    {
        /// <summary>
        /// 读取登入信息
        /// </summary>
        /// <returns>用户名 密码</returns>
        public static String[] readSaveLoginInfo()
        {
            //读取配置文件内容
            FileStream fileStream = new FileStream(Global.loginInfoPath, FileMode.Open, FileAccess.ReadWrite, FileShare.ReadWrite);
            StreamReader reader = new StreamReader(fileStream);
            String content = reader.ReadToEnd();
            reader.Close();
            fileStream.Close();
            return content.Split('\n');
        }

        /// <summary>
        /// 写入登入信息
        /// </summary>
        /// <param name="uid">用户名</param>
        /// <param name="pwd">密码</param>
        public static void writeSaveLoginInfo(String uid, String pwd)
        {
            if (uid == String.Empty || pwd == String.Empty)
            {
                return;
            }
            else
            {
                FileStream fileStream = new FileStream(Global.loginInfoPath, FileMode.Open, FileAccess.ReadWrite, FileShare.ReadWrite);
                byte[] buffer = System.Text.Encoding.UTF8.GetBytes(uid + '\n' + pwd);
                fileStream.Write(buffer, 0, buffer.Length);
                fileStream.Flush();
                fileStream.Close();
            }
        }

        /// <summary>
        /// 清除登入信息
        /// </summary>
        public static void clearSaveLoginInfo()
        {
            File.Delete(Global.loginInfoPath);
            File.Create(Global.loginInfoPath).Close();
        }
    }
}
