DROP TABLE IF EXISTS public.reservation;
DROP TABLE IF EXISTS public.property;
DROP TABLE IF EXISTS public."user";

CREATE TABLE public.app_user(
                       id uuid NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id));

CREATE TABLE public.property(
                         id uuid NOT NULL,
                         user_id uuid NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (user_id) REFERENCES public.app_user(id));

CREATE TABLE public.reservation(
                            id uuid NOT NULL,
                            user_id uuid NOT NULL,
                            property_id uuid NOT NULL,
                            start_date DATE NOT NULL,
                            end_date DATE NOT NULL,
                            type VARCHAR(40),
                            PRIMARY KEY (id),
                            FOREIGN KEY (property_id) REFERENCES public.property(id),
                            FOREIGN KEY (user_id) REFERENCES public.app_user(id));