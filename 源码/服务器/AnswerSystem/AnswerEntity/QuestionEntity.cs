using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AnswerEntity
{
    public class QuestionEntity
    {
        public QuestionEntity(long id, String content, String option1, String option2, String option3, String option4, sbyte passOption, int score)
        {
            this.id = id;
            this.content = content;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
            this.option4 = option4;
            this.passOption = passOption;
            this.score = score;
        }

        #region 字段
        //题目ID
        private long id;
        //题目以及四个选项内容
        private String content, option1, option2, option3, option4;
        //正确选项
        private sbyte passOption;
        //分值
        private int score;

        #endregion

        #region 属性

        public long Id
        {
            get { return id; }
            set { id = value; }
        }
        public String Option4
        {
            get { return option4; }
            set { option4 = value; }
        }

        public String Option3
        {
            get { return option3; }
            set { option3 = value; }
        }

        public String Option2
        {
            get { return option2; }
            set { option2 = value; }
        }

        public String Option1
        {
            get { return option1; }
            set { option1 = value; }
        }

        public String Content
        {
            get { return content; }
            set { content = value; }
        }


        public sbyte PassOption
        {
            get { return passOption; }
            set { passOption = value; }
        }

        public int Score
        {
            get { return score; }
            set { score = value; }
        }
        #endregion
    }
}
