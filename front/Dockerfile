FROM node:18.16.0 AS  build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
FROM nginx:alpine
# ng build --configuration=production
COPY --from=build /app/dist/dashboard-robot-agv /usr/share/nginx/html
EXPOSE 4200