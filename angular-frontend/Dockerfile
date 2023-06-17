FROM node:18

WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install

COPY ./ ./

RUN npm run build

FROM flashspys/nginx-static:latest

ENV BACKEND_URL="http://localhost:80"
WORKDIR /usr/src

COPY --from=0 /app/dist/fulltext-across-datasources-frontend /static
COPY --from=0 /app/env.sh /app/.env* /usr/src/
RUN chmod +x env.sh

CMD ["/bin/ash", "-c", "/usr/src/env.sh && cp /usr/src/env-config.js /static && nginx -g \"daemon off;\""]
