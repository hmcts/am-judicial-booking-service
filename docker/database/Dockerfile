FROM postgres:15.13
USER postgres
COPY init-database.sh /docker-entrypoint-initdb.d

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD psql -c 'select 1' -d judicial_booking -U ${JUDICIAL_BOOKING_SERVICE_POSTGRES_USER}

EXPOSE 5432
