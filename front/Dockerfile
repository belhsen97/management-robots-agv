FROM node:18.16.0 AS  build
WORKDIR /app
COPY . .

RUN npm install

RUN npm run build

# Serve Application using Nginx Server

FROM nginx:alpine

COPY --from=build /app/dist/dashboard-robot-agv /usr/share/nginx/html

EXPOSE 4200