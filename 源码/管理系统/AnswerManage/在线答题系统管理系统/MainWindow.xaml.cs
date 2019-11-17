using AnswerEntity;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.OleDb;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace 在线答题系统管理系统
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void btnSelectUserFile_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog selectFile = new OpenFileDialog();
            bool result = (bool)selectFile.ShowDialog();
            if (result)
            {
                txtUserFilePath.Text = selectFile.FileName.ToString();
            }
        }

        private void btnSelectQuestionFile_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog selectFile = new OpenFileDialog();
            bool result = (bool)selectFile.ShowDialog();
            if (result)
            {
                txtQuestionFilePath.Text = selectFile.FileName.ToString();
            }
        }

        private void btnQuery_Click(object sender, RoutedEventArgs e)
        {
            String uid = txtQueryUid.Text;
            if (uid == String.Empty)
            {
                MessageBox.Show("不可为空！");
            }
            else
            {
                UserEntity entity = WebUtil.getUserInfo(Global.token, uid);
                if (entity == null)
                {
                    txtName.Text = String.Empty;
                    txtClassName.Text = String.Empty;
                    txtComplete.Text = String.Empty;
                    txtIsAdmin.Text = String.Empty;
                    txtLastLoginTime.Text = String.Empty;
                    txtLock.Text = String.Empty;
                    txtRegTime.Text = String.Empty;
                    MessageBox.Show("没有找到这个账号！请检查输入", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
                txtName.Text = entity.name;
                txtClassName.Text = entity.className;
                txtComplete.Text = entity.isComplete ? "已完成" : "未完成";
                txtIsAdmin.Text = entity.isAdmin ? "管理员" : "普通用户";
                txtLastLoginTime.Text = entity.lastLoginTime.ToString();
                txtLock.Text = entity.locking ? "已锁定" : "未锁定";
                txtRegTime.Text = entity.regTime.ToString();
            }
        }

        private void btnReg_Click(object sender, RoutedEventArgs e)
        {
            String filePath = txtUserFilePath.Text.ToString().Trim();
            if (filePath == String.Empty)
            {
                MessageBox.Show("请填写好参数！");
            }
            else
            {
                if (!File.Exists(filePath))
                {
                    MessageBox.Show("文件不存在！");
                    return;
                }
                new Thread(() =>
                {
                    OleDbConnection oledb = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath + ";Extended Properties='Excel 8.0;HDR=NO;IMEX=1';");
                    try
                    {
                        oledb.Open();
                        using (OleDbDataAdapter oledbcmd = new OleDbDataAdapter("Select * From [Sheet1$]", oledb))
                        {
                            DataSet dataset = new DataSet();
                            oledbcmd.Fill(dataset);
                            if (dataset.Tables[0].Rows.Count - 1 < 0)
                            {
                                throw new Exception("没有任何数据行~");
                            }
                            //设置进度条Max
                            this.Dispatcher.Invoke(() =>
                            {
                                pbRegProgress.Maximum = dataset.Tables[0].Rows.Count;
                                btnReg.IsEnabled = false;
                            });
                            regUser(dataset);
                        }
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show(ex.Message);
                    }
                    finally
                    {
                        oledb.Close();
                    }
                }).Start();
            }
        }

        private void btnUploadQuestion_Click(object sender, RoutedEventArgs e)
        {
            //文件路径
            String filePath = txtQuestionFilePath.Text.ToString().Trim();
            if (filePath == String.Empty)
            {
                MessageBox.Show("请填写好参数！");
            }
            else
            {
                if (!File.Exists(filePath))
                {
                    MessageBox.Show("文件不存在！");
                    return;
                }
                new Thread(() =>
                {
                    //读取文件
                    using (OleDbConnection oledb = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + filePath + ";Extended Properties='Excel 8.0;HDR=NO;IMEX=1';"))
                    {
                        oledb.Open();
                        using (OleDbDataAdapter oldDBAdapter = new OleDbDataAdapter("Select * From [Sheet1$]", oledb))
                        {
                            //数据集
                            DataSet dataSet = new DataSet();
                            //将结果填充到数据集当中
                            oldDBAdapter.Fill(dataSet, "data");
                            //Check Data
                            int count = dataSet.Tables["data"].Rows.Count;
                            if (count == 1)
                            {
                                this.Dispatcher.Invoke(() =>
                                {
                                    MessageBox.Show("没有任何数据！");
                                });
                                return;
                            }
                            this.Dispatcher.Invoke(() =>
                            {
                                pbQuestionProssgress.Maximum = count;
                                btnSelectQuestionFile.IsEnabled = false;
                                btnUploadQuestion.IsEnabled = false;
                            });
                            bool isFirst = true;
                            int number = 0;
                            //上传
                            foreach (DataRow row in dataSet.Tables["data"].Rows)
                            {
                                number++;
                                if (isFirst)
                                {
                                    this.Dispatcher.Invoke(() =>
                                    {
                                        pbQuestionProssgress.Value++;
                                    });
                                    isFirst = false;
                                    //进入到下一行
                                    continue;
                                }
                                //上传
                                String content = row[0].ToString().Trim();
                                String optionAStr = row[1].ToString().Trim();
                                optionAStr = (optionAStr == String.Empty) ? " " : optionAStr;
                                String optionBStr = row[2].ToString().Trim();
                                optionBStr = (optionBStr == String.Empty) ? " " : optionBStr;
                                String optionCStr = row[3].ToString().Trim();
                                optionCStr = (optionCStr == String.Empty) ? " " : optionCStr;
                                String optionDStr = row[4].ToString().Trim();
                                optionDStr = (optionDStr == String.Empty) ? " " : optionDStr;
                                char passOptionChar = row[5].ToString().Trim().ToUpper().ToArray()[0];
                                int passOption = 0;
                                switch (passOptionChar)
                                {
                                    case 'A':
                                        passOption = 1;
                                        break;
                                    case 'B':
                                        passOption = 2;
                                        break;
                                    case 'C':
                                        passOption = 3;
                                        break;
                                    case 'D':
                                        passOption = 4;
                                        break;
                                }
                                int score = int.Parse(row[6].ToString().Trim());
                                //上传
                                QuestionEntity entity = new QuestionEntity() { content = content, option1 = optionAStr, option2 = optionBStr, option3 = optionCStr, option4 = optionDStr, passOption = (sbyte)passOption, score = score };
                                bool result = WebUtil.uploadQuestion(Global.token, entity);
                                if (result)
                                {
                                    this.Dispatcher.Invoke(() =>
                                    {
                                        pbQuestionProssgress.Value++;
                                    });
                                }
                                else
                                {
                                    Console.WriteLine("error:" + number);
                                }
                            }
                            this.Dispatcher.Invoke(() =>
                            {
                                MessageBox.Show(String.Format("总共：{0}条数据，失败：{1}条，成功：{2}条。", pbQuestionProssgress.Maximum, pbQuestionProssgress.Maximum - pbQuestionProssgress.Value, pbQuestionProssgress.Value));
                                btnSelectQuestionFile.IsEnabled = true;
                                btnUploadQuestion.IsEnabled = true;
                                pbQuestionProssgress.Value = 0;
                                pbQuestionProssgress.Maximum = 100;
                            });
                        }
                    }
                }).Start();
            }
        }

        private void regUser(DataSet dataset)
        {
            bool isFirst = false;
            foreach (DataRow row in dataset.Tables[0].Rows)
            {
                if (!isFirst)
                {
                    this.Dispatcher.Invoke(() =>
                    {
                        pbRegProgress.Value++;
                    });
                    isFirst = true;
                    continue;
                }
                //上传
                //账号 密码 姓名 身份
                String uid = row[0].ToString().Trim();
                String pwd = row[1].ToString().Trim();
                String name = row[2].ToString().Trim();
                String className = row[3].ToString().Trim();
                bool isadmin = row[4].ToString().Trim() == "1";
                bool result = WebUtil.regUser(new UserEntity() { uid = uid, pwd = pwd, name = name, className = className, isAdmin = isadmin });
                if (result)
                {
                    this.Dispatcher.Invoke(() =>
                    {
                        pbRegProgress.Value++;
                    });
                }
            }
            this.Dispatcher.Invoke(() =>
            {
                MessageBox.Show(String.Format("总共：{0}条数据，失败：{1}条，成功：{2}条。", pbRegProgress.Maximum, pbRegProgress.Maximum - pbRegProgress.Value, pbRegProgress.Value));
                btnReg.IsEnabled = true;
                pbRegProgress.Value = 0;
            });
        }
    }
}
