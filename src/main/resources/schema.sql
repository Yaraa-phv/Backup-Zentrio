-- Create the users table
CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       username VARCHAR(50) NOT NULL,
                       gender VARCHAR(100) ,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR NOT NULL,
                       provider VARCHAR,
                       profile_image VARCHAR DEFAULT 'https://i.pinimg.com/736x/d0/7b/a6/d07ba6dcf05fa86c0a61855bc722cb7a.jpg',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_verified BOOLEAN DEFAULT false,
                       is_reset BOOLEAN DEFAULT false
);