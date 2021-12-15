DELETE FROM booking;
INSERT INTO public.booking (id,user_id,location_id,region_id,begin_time,end_time,created) VALUES
	 ('efae8b33-97eb-4469-8715-516114517193','631d322c-eea7-4d53-bd92-e6ec51bcb390','BA1','south-east',(current_date -1),current_date,now()),
	 ('d75816a3-76be-4037-b15a-e68283f054b6','631d322c-eea7-4d53-bd92-e6ec51bcb390','B1','south-west',current_date,current_date+1,now()),
	 ('fa0d040e-aa56-4247-a6b1-349d0a1d3249','631d322c-eea7-4d53-bd92-e6ec51bcb390',NULL,NULL,current_date+2,current_date+5,now()),
	 ('a6d25ce9-8eb8-4254-968e-e4679bfe9636','631d322c-eea7-4d53-bd92-e6ec51bcb390','BA1','south-west',current_date-5,current_date-2,now()),
	 ('4e06dbbb-fe65-482b-b3e6-c902cf36c76a','123e4567-e89b-42d3-a456-556642445678','BAfuture','south-east',current_date,current_date+1,now());
