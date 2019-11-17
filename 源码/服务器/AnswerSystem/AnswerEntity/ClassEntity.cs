using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AnswerEntity
{
    public class ClassEntity
    {
        public ClassEntity(int id,String className)
        {
            this.ClassId = id;
            this.className = className;
        }

        private int ClassId;
        private String className;

        public int ClassID
        {
            get { return ClassId; }
            set { ClassId = value; }
        }
        

        public String ClassName
        {
            get { return className; }
            set { className = value; }
        }
    }
}
