DROP DATABASE IF EXISTS foodjoa;
CREATE DATABASE foodjoa;

USE foodjoa;

-- member ------------------------------------------------------------------------------
DROP TABLE IF EXISTS member;
CREATE TABLE member(
    id 			varchar(20) primary key not null,
    name 		varchar(10) not null,
    nickname 	varchar(10) not null,
    phone 		varchar(15) not null,
    address 	varchar(50) not null,
    profile 	varchar(50) not null,
    join_date 	timestamp not null
);

SELECT * FROM member;

DESC member;

insert into member
values('admin', '관리자', '고나리자', '01012345678', '부산시 부산진구', 'admin_image.png', CURRENT_TIMESTAMP);


-- -------------------------------------------------------------------------------------


-- recipe ------------------------------------------------------------------------------
DROP TABLE IF EXISTS recipe;
CREATE TABLE recipe(
	no 					int primary key auto_increment,
    id 					varchar(20) not null,
    title 				varchar(50) not null,
    thumbnail 			varchar(50) not null,
    description			varchar(100) not null,
    contents 			text not null,
    category 			tinyint not null,
    views 				int not null,
    rating 				float not null,
    ingredient 			varchar(255) not null,
    ingredient_amount 	varchar(255) not null,
    orders 				varchar(255) not null,
    empathy 			int not null,
    post_date			timestamp not null,
    
    FOREIGN KEY (id) REFERENCES member(id)
);

insert into recipe(id, title, thumbnail, 
	description, contents, category, views, 
	rating, ingredient, ingredient_amount, 
	orders, empathy, post_date) 
values('admin', '레시피 제목', 'thumbnailImage.png',
	'레시피 간단 설명', '레시피 설명', 0, 0,
    4.5, '물@멸치액젓@고춧가루', '500ml@0.5 큰술@1 큰술', 
	'물을 끓인다@스프와 멸치액젓, 고춧가루를 넣는다@물이 끓으면 면을 넣는다', 3, CURRENT_TIMESTAMP);

desc recipe;
select * from recipe;

drop table if exists recipe_review;
create table recipe_review(
	no 				int primary key auto_increment,
    id 				varchar(20) not null,
    recipe_no 		int not null,
    pictures 		text not null,
    contents 		text not null,
    ration 			float not null,
    empathy 		int not null,
    post_date		timestamp,
    
    FOREIGN KEY (id) REFERENCES member(id),
    FOREIGN KEY (recipe_no) REFERENCES recipe(no)
);

desc recipe_review;
select * from recipe_review;

drop table if exists recipe_wishlist;
create table recipe_wishlist(
	no 			int primary key auto_increment,
    id 			varchar(20) not null,
    recipe_no 	int not null, 
    
    FOREIGN KEY (id) REFERENCES recipe_reviewrecipe_reviewrecipe_wishlistrecipe_wishlistmember(id),
    FOREIGN KEY (recipe_no) REFERENCES recipe(no)
);

desc recipe_wishlist;
select * from recipe_wishlist;

-- -------------------------------------------------------------------------------------


-- mealkit ------------------------------------------------------------------------------
DROP TABLE IF EXISTS mealkit;
CREATE TABLE mealkit(
    no            int primary key auto_increment,
	id            varchar(20) not null,
	title         varchar(50) not null,
	content       text not null,
	category      tinyint not null,
	price         varchar(10) not null,
    amount		int not null,
	pictures      text not null,
	orders        varchar(255) not null,
	origin        varchar(255) not null,
	rating        float not null,
	views         int not null,
	soldout       tinyint not null,
	post_date     timestamp not null,

    foreign key (id) references member(id)
);
SELECT * FROM mealkit;

DESC mealkit;

-- mealkit_order
DROP TABLE IF EXISTS mealkit_order;
CREATE TABLE mealkit_order(
	no			int primary key auto_increment,
    id			varchar(20) not null,
    mealkit_no	int not null,
    address		varchar(5) not null,
    delivered	tinyint not null,
    refund		tinyint not null,
    post_date	timestamp not null,
    
    foreign key (id) references member(id),
    foreign key (mealkit_no) references mealkit(no)
);

SELECT * FROM mealkit_order;

DESC mealkit_order;

-- mealkit_review
DROP TABLE IF EXISTS mealkit_review;
CREATE TABLE mealkit_review(
	no			int primary key auto_increment,
    id			varchar(20) not null,
    mealkit_no	int not null,
    pictures	text not null,
    contents	text not null,
    rating		float not null,
    empathy		int not null,
    post_date	timestamp not null,
    
    foreign key (id) references member(id),
    foreign key (mealkit_no) references mealkit(no)
);

SELECT * FROM mealkit_review;

DESC mealkit_review;

-- mealkit_wishlist
DROP TABLE IF EXISTS mealkit_wishlist;
CREATE TABLE mealkit_wishlist(
	no			int primary key auto_increment,
    id			varchar(20) not null,
    mealkit_no	int not null,
    type		tinyint not null,
    
    foreign key (id) references member(id),
    foreign key (mealkit_no) references mealkit(no)
);

SELECT * FROM mealkit_wishlist;

DESC mealkit_wishlist;


-- -------------------------------------------------------------------------------------


-- community ------------------------------------------------------------------------------




-- -------------------------------------------------------------------------------------
