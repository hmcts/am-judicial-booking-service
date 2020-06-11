FROM postgres:9.6

COPY init-db.sh /docker-entrypoint-initdb.d

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD psql -c 'select 1' -d judicial_booking -U ${AM_JUDICIAL_BOOKING_DB_USERNAME}

EXPOSE 5455
