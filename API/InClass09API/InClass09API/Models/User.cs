using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace InClass09API.Models
{
    public class UserData
    {
        public string id { get; set; }
        public string first_name { get; set; }
        public string last_name { get; set; }
        public string email { get; set; }
        public string ip_address { get; set; }
        public string gender { get; set; }
    }

    public enum Sort
    {
        FirstName,
        LastName,
        Gender
    }
}