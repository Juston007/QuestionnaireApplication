using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DBHelp;
using AnswerEntity;
using System.Collections;
using System.Threading;


namespace Test
{
    class Program
    {
        static void Main(string[] args)
        {
            HelpUtil.ConfigUtil.ConnectionStr = "Server=.;DataBase=AnswerDB;Uid=sa;Password=123456";
            Random random = new Random();


            new Thread(() => {
                String uid = "20160101";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();

            new Thread(() =>
            {
                String uid = "20160102";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();


            new Thread(() =>
            {
                String uid = "20160103";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();


            new Thread(() =>
            {
                String uid = "20160104";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();


            new Thread(() =>
            {
                String uid = "20160105";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();

            new Thread(() =>
            {
                String uid = "20160106";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();

            new Thread(() =>
            {
                String uid = "20160107";
                int id = 0;
                DBUtil.startAnswer(uid, out id);
                Thread.Sleep(random.Next(10000));
                int grade = random.Next(100);
                DBUtil.submitAnswer(uid, id, grade);
                Console.WriteLine("UID:{0},ID:{1},GRADE:{2}", uid, id, grade);
            }).Start();

           



        }
    }
}
