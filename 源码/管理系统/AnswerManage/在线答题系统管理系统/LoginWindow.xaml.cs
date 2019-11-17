using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using AnswerEntity;
using System.IO;

namespace 在线答题系统管理系统
{
    /// <summary>
    /// LoginWindow.xaml 的交互逻辑
    /// </summary>
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
        }

        private void btnLogin_Click(object sender, RoutedEventArgs e)
        {
            String uid = txtUid.Text.ToString().Trim();
            String pwd = txtPwd.Password.ToString().Trim();

            if (String.IsNullOrEmpty(uid) || String.IsNullOrEmpty(pwd))
            {
                MessageBox.Show("请输入账号密码！");
            }
            else
            {
                String result = "";
                if (WebUtil.getToken(uid, pwd, out result))
                {
                    Global.token = result;
                    UserEntity loginUser = WebUtil.getUserInfo(Global.token, uid);
                    if (loginUser.isAdmin)
                    {
                        if ((bool)cbRememberMe.IsChecked)
                        {
                            LoginInfoUtil.writeSaveLoginInfo(uid, pwd);
                        }
                        else
                        {
                            LoginInfoUtil.clearSaveLoginInfo();
                        }
                        new MainWindow().Show();
                        Close();
                    }
                    else
                    {
                        MessageBox.Show("权限不足！");
                    }
                }
                else
                {
                    MessageBox.Show(result);
                }
            }
        }

        private void Window_Loaded_1(object sender, RoutedEventArgs e)
        {
            //读取服务器地址
            Global.serverUrl = System.Configuration.ConfigurationManager.AppSettings["ServerAddress"].ToString();
            //读取登入信息
            String[] loginInfo = LoginInfoUtil.readSaveLoginInfo();
            if (loginInfo == null)
            {
                return;
            }
            else if (loginInfo.Length == 2)
            {
                txtUid.Text = loginInfo[0];
                txtPwd.Password = loginInfo[1];
                cbRememberMe.IsChecked = true;
            }
        }
    }
}
