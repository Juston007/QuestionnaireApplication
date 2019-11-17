using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HelpUtil
{
    public class ConfigUtil
    {
        private static String connectionStr = "";

        public static String ConnectionStr
        {
            get { return ConfigUtil.connectionStr; }
            set { ConfigUtil.connectionStr = value; }
        }
    }
}
