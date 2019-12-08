# Installation

```
export VERSION=`git rev-parse HEAD`
export OAUTH_TOKEN_URL=http://localhost:8090/auth/realms/samply/protocol/openid-connect/token
./mvnw -B clean package
eval $(minikube docker-env)
docker build -t local/samply/catalog-api:${VERSION} .
helm package ./helm/catalog-api --app-version $VERSION
helm upgrade --install catalog-api ./catalog-api-0.0.1.tgz --wait
kubectl port-forward svc/catalog-api 8080:8080
```

## Manual istio sidecar injection

```
helm template ./catalog-api-0.0.1.tgz -x templates/deployment.yaml -n catalog-api | istioctl kube-inject -f - | kubectl apply -f -
```

## TODO
