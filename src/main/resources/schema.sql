-- DATABASE SCRIPT
-- Create database
CREATE DATABASE zentrio_db;

--Create extension for uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the users table
CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       username VARCHAR(50) NOT NULL,
                       gender VARCHAR(100) ,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR NOT NULL,
                       profile_image VARCHAR DEFAULT 'https://i.pinimg.com/736x/d0/7b/a6/d07ba6dcf05fa86c0a61855bc722cb7a.jpg',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_verified BOOLEAN DEFAULT false,
                       is_reset BOOLEAN DEFAULT false
);

-- Create the roles table
CREATE TABLE roles (
                       role_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       role_name VARCHAR(50) NOT NULL
);

INSERT INTO roles(role_name) VALUES ('ROLE_MANAGER'), ('ROLE_LEADER'), ('ROLE_MEMBER');

-- Create the workspaces table
CREATE TABLE workspaces (
                            workspace_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            title VARCHAR(255) NOT NULL,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            created_by UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Create the boards table
CREATE TABLE boards (
                        board_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        cover VARCHAR,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        is_favourite BOOLEAN DEFAULT false,
                        is_verified BOOLEAN DEFAULT false,
                        workspace_id UUID REFERENCES workspaces(workspace_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the gantt_charts table
CREATE TABLE gantt_charts (
                              gantt_chart_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              title VARCHAR(255) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              board_id UUID UNIQUE REFERENCES boards(board_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the gantt_bars table
CREATE TABLE gantt_bars (
                            gantt_bar_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            title VARCHAR(255) NOT NULL,
                            started_at TIMESTAMP NOT NULL,
                            finished_at TIMESTAMP NOT NULL,
                            gantt_chart_id UUID REFERENCES gantt_charts(gantt_chart_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the members table
CREATE TABLE members (
                         member_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         role_id UUID REFERENCES roles(role_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                         user_id UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                         board_id UUID REFERENCES boards(board_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the tasks table
CREATE TABLE tasks (
                       task_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       is_done BOOLEAN DEFAULT false,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       started_at TIMESTAMP NOT NULL,
                       finished_at TIMESTAMP NOT NULL,
                       task_order SERIAL,
                       stage VARCHAR(50) NOT NULL,
                       board_id UUID REFERENCES boards(board_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                       gantt_bar_id UUID REFERENCES gantt_bars(gantt_bar_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the task_assignment table
CREATE TABLE task_assignment (
                                 task_assign_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 assigned_to UUID REFERENCES members(member_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                                 assigned_by UUID REFERENCES members(member_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                                 task_id UUID REFERENCES tasks(task_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the checklists table
CREATE TABLE checklists (
                            checklist_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            title VARCHAR(255) NOT NULL,
                            is_done BOOLEAN DEFAULT false,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
                            checklist_order SERIAL,
                            started_at TIMESTAMP,
                            finished_at TIMESTAMP,
                            task_id UUID REFERENCES tasks(task_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the checklist_items table
CREATE TABLE checklist_items (
                                 checklist_item_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 title VARCHAR(255) NOT NULL,
                                 status VARCHAR(50),
                                 checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the checklist_assignments table
CREATE TABLE checklist_assignments (
                                       checklist_assign_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       member_id UUID REFERENCES members(member_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                                       assigned_by UUID REFERENCES members(member_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                                       checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the labels table
CREATE TABLE labels (
                        label_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        label_title VARCHAR(50) NOT NULL,
                        label_color VARCHAR(20),
                        checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the calendars table
CREATE TABLE calendars (
                           calendar_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           noted TEXT,
                           noted_at TIMESTAMP NOT NULL ,
                           till_date TIMESTAMP ,
                           user_id UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                           checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE
);



-- Create the comments table
CREATE TABLE comments (
                          comment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          content TEXT NOT NULL,
                          comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                          commented_by UUID REFERENCES checklist_assignments(checklist_assign_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the documents table
CREATE TABLE documents (
                           document_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           created_at TIMESTAMP ,
                           is_private BOOLEAN DEFAULT false,
                           doc_type VARCHAR(50),
                           user_id UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                           board_id UUID REFERENCES boards(board_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the attachment table
CREATE TABLE attachment (
                            attachment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            details JSONB,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            checklist_id UUID REFERENCES checklists(checklist_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the achievement table
CREATE TABLE achievement (
                             achievement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             details JSONB,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             user_id UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Create the notifications table
CREATE TABLE notifications (
                               notification_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               content VARCHAR ,
                               type VARCHAR(50),
                               is_read BOOLEAN DEFAULT false,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               task_assign_id UUID REFERENCES task_assignment(task_assign_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                               user_id UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the report table
CREATE TABLE report (
                        report_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        board_id UUID REFERENCES boards(board_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the feedback table
CREATE TABLE feedback (
                          feedback_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          comment VARCHAR(255),
                          task_id UUID REFERENCES tasks(task_id) ON DELETE CASCADE ON UPDATE CASCADE ,
                          feedback_by UUID REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);


