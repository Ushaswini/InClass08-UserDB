using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using InClass09API.Models;

namespace InClass09API.Controllers
{
    public class UserDataController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: api/UserData
        public IQueryable<UserData> GetUsers()
        {
            return db.Users;
        }

        public IList<UserData> GetUsersForLogic(int index, Sort sort)
        {
            var count = db.Users.Count();

            if (index > 0 && index < 21)
            {
                IQueryable<UserData> result;
                switch (sort)
                {
                    case Sort.FirstName:
                        {
                            result = db.Users.OrderBy(u => u.first_name);
                            break;
                        }
                    case Sort.LastName:
                        {
                            result = db.Users.OrderBy(u => u.last_name);
                            break;
                        }
                    case Sort.Gender:
                        {
                            result = db.Users.OrderBy(u => u.gender);
                            break;
                        }
                    default:
                        {
                            result = db.Users.OrderBy(u => u.id);
                            break;
                        }

                }

                //var r = result.ToList().GetRange((index - 1) * 50, index * 50);
                var r = result.Skip((index - 1) * 50).Take(50).ToList();

                return r;
            }
            else
            {
                return new List<UserData>();
            }


        }

        // GET: api/UserData/5
        [ResponseType(typeof(UserData))]
        public IHttpActionResult GetUserData(string id)
        {
            UserData userData = db.Users.Find(id);
            if (userData == null)
            {
                return NotFound();
            }

            return Ok(userData);
        }

        // PUT: api/UserData/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutUserData(string id, UserData userData)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != userData.id)
            {
                return BadRequest();
            }

            db.Entry(userData).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserDataExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/UserData
        [ResponseType(typeof(UserData))]
        public IHttpActionResult PostUserData(UserData userData)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Users.Add(userData);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (UserDataExists(userData.id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = userData.id }, userData);
        }

        // DELETE: api/UserData/5
        [ResponseType(typeof(UserData))]
        public IHttpActionResult DeleteUserData(string id)
        {
            UserData userData = db.Users.Find(id);
            if (userData == null)
            {
                return NotFound();
            }

            db.Users.Remove(userData);
            db.SaveChanges();

            return Ok(userData);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UserDataExists(string id)
        {
            return db.Users.Count(e => e.id == id) > 0;
        }
    }
}