using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AnswerEntity
{
    public class AnswerRecordEntity
    {
        public AnswerRecordEntity(int id, int grade, String name, String startTime, String submitTime)
        {
            this.id = id;
            this.grade = grade;
            this.name = name;
            this.startTime = DateTime.Parse(startTime == String.Empty ? "1970/01/01" : startTime);
            this.submitTime = DateTime.Parse(submitTime == String.Empty ? "1970/01/01" : submitTime);
        }

        #region 字段

        private int id, grade;
        private String name,uid;
        private DateTime startTime, submitTime;

        #endregion

        #region 属性

        public String Uid
        {
            get { return uid; }
            set { uid = value; }
        }

        public int Grade
        {
            get { return grade; }
            set { grade = value; }
        }

        public int Id
        {
            get { return id; }
        }

        public String Name
        {
            get { return name; }
        }

        public DateTime SubmitTime
        {
            get { return submitTime; }
            set { submitTime = value; }
        }

        public DateTime StartTime
        {
            get { return startTime; }
        }
        #endregion
    }
}
