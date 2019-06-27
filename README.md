# Ratpack demo of Resilience4j


This demo shows how to use the fault tolerance library [Resilience4j](https://github.com/resilience4j/resilience4j) in a [Ratpack](https://www.ratpack.io) application.

See [User Guide](https://resilience4j.readme.io/docs/getting-started-5) for more details.

## Requirements
[Docker](https://docs.docker.com/install/) and [Docker Compose](https://docs.docker.com/compose/install/) installed.

## Getting Started

1. Use docker-compose to start grafana and prometheus servers.
- In the root folder:
```bash
docker-compose -f docker-compose.yml up -d
```

2. Start the demo project through the main class.
```bash
./gradlew run
```

3. Check the application metrics:
- Open http://localhost:5050/actuator/prometheus
- Observe the collected prometheus metrics

4. Check the status of prometheus:
- Open http://localhost:9090
- Access status -> Targets, both endpoints must be "UP"

5. View metrics in grafana:
- Open http://localhost:3000
- Log in with admin/admin
- Navigate to the "Resilience4j Ratpack Demo" dashboard

6. Run the tests and watch the metrics update.
```bash
./gradlew test
```

## License

Copyright 2019 Dan Maas

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
