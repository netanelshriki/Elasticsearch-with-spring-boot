# Elasticsearch-with-spring-boot
a demo application for spring boot with elasticsearch

## step 1

```bash
docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
```
