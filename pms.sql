alter table business_opportunities 
add column lostDate datetime default null , 
add column completedDate datetime default null ;

create table business_opportunity_remarks(
    id int(11) NOT NULL AUTO_INCREMENT,
    business_opportunity_id int(11) NOT NULL,
    progress int NOT NULL,
    content text default null,
    created_at date not null,
    PRIMARY KEY (id)
);