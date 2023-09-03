# run docker-compose.yml

# execute script -> src/main/resources/scripts/init.sql

endpoints:

* post **/api/users** to create a user
##### {
    "firstName": "user",
    "lastName": "lastName",
    "emails":[{"email":"test@mail.com"}]
#### }
return userId

* post **/api/users/phone** to add phone to user id
#### {
        "userId" : 1,
        "phone": "12345678"
#### }

* post **/api/users/email** to add phone to user id
#### {
        "userId" : 1,
        "email": "test@mail.com"
#### }

* get **/api/users** returns users
* get **/api/users/contacts/{userId}** returns user contacts
* get **/api/users/phones/{userId}** returns user phones
* get **/api/users/emails/{userId}** returns user emails

* delete **/api/users/{id}** delete user with contacts
* delete **/api/users/email/{emailId}** delete user email by email id
* delete **/api/users/phone/{phoneId}** delete user phone by phone id
