INSERT INTO public.app_user (id, username, password)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c7825100', 'foo@gmail.com', '$2a$12$gFf5Ler8sSpNdE.1SVZSoeSwVa2d5ke4NPtzqYB6J9pjegy8IeoQi');
INSERT INTO public.app_user (id, username, password)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c7825101', 'johndoe@gmail.com', '$2a$12$gFf5Ler8sSpNdE.1SVZSoeSwVa2d5ke4NPtzqYB6J9pjegy8IeoQi');
INSERT INTO public.app_user (id, username, password)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c7825102', 'newguest@gmail.com', '$2a$12$gFf5Ler8sSpNdE.1SVZSoeSwVa2d5ke4NPtzqYB6J9pjegy8IeoQi');

INSERT INTO public.property (id, user_id, name)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251b1', select id from app_user where username = 'foo@gmail.com', 'house 1');
INSERT INTO public.property (id, user_id, name)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251b2', select id from app_user where username = 'johndoe@gmail.com', 'house 2');
INSERT INTO public.property (id, user_id, name)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251b3', select id from app_user where username = 'johndoe@gmail.com', 'house 3');


INSERT INTO public.reservation (id, user_id,  property_id, start_date, end_date, type)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251c1',
           select id from app_user where username = 'foo@gmail.com',
           select id from property where name = 'house 1',
        CURRENT_DATE() + 10,
        CURRENT_DATE + 15,
        'BLOCK');

INSERT INTO public.reservation (id, user_id,  property_id, start_date, end_date, type)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251c2',
        select id from app_user where username = 'foo@gmail.com',
        select id from property where name = 'house 2',
        CURRENT_DATE() + 10,
        CURRENT_DATE + 15,
        'BOOKING');

INSERT INTO public.reservation (id, user_id,  property_id, start_date, end_date, type)
VALUES ('dbd76c0a-cb18-4d48-be34-9cc9c78251c3',
               select id from app_user where username = 'johndoe@gmail.com',
               select id from property where name = 'house 2',
        CURRENT_DATE() + 18,
        CURRENT_DATE + 20,
        'BLOCK');

-- house 1 owner is foo@gmail.com
-- house 2 and house 3 owner is johndoe@gmail.com
-- there is 1 BLOCK reservation done on house 1 by the owner foo@gmail.com
-- there is 1 BLOCK reservation done on house 2 by the owner johndoe@gmail.com
-- there is 1 BOOKING reservation done on house 2 by the guest foo@gmail.com
-- newguest@gmail.com has no properties and no reservations.
-- house 3 has no reservations nor blocks.