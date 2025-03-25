# Stage 1: Build
FROM node:22-slim AS builder

WORKDIR /app
COPY . .
RUN npm install && npm run build && npm run lint

# Stage 2: Serve using a lightweight web server
FROM node:22-slim

WORKDIR /app
RUN npm install -g serve
COPY --from=builder /app/dist ./dist

EXPOSE 5173
CMD ["serve", "-s", "dist", "-l", "5173"]
