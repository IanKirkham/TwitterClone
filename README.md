# TwitterClone
TwitterClone is an Android front-end with an AWS back-end. Clones the basics functionality of Twitter.

## Features
- Register/Login with proper authentication
- Post status updates, complete with user mentions and url spanning
- Follow/Unfollow users
- Browse the stories of other users

## Goals
1. Hands-on experience with a variety of design patterns
2. Basic understanding of fundamental AWS technologies

## Design Patterns
1. **Template Method**: code duplication is diminished across similar fragments, such as Feed/Story and Followers/Followees
2. **Lazy Initialization**: profile pictures are only loaded when needed
3. **Facade**: a ServerFacade on the front-end abstracts away the details of the back-end
4. **Proxy**: ServiceProxies control access to the ServerFacade
5. **Observer**: view presenters are registered as observers for the asyncronous calls to the back-end

## AWS Technologies
1. **API Gateway**: the front-end accesses the back-end through a basic API
2. **Lambda**: the handlers receive requests from API Gateway, access data from DynamoDB, and return responses back to the front-end
3. **DynamoDB**: stores data in User, AuthToken, Follows, Story and Feed tables
4. **Simple Queue Service**: high volume requests, such as writing a status to many user feeds, are handled in parallel, batched writes. These high volume requests are batched by lambda handlers and written to an SQS queue, then received and written to DynamoDB by another lambda handler
5. **S3**: profile pictures are uploaded and stored in S3 by the Register lambda handler
