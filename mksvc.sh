rm -Rf svc
set -x
unzip -d svc target/universal/*-SNAPSHOT.zip
mv svc/*/* svc/
rm svc/bin/*.bat
mv svc/bin/* svc/bin/start
docker build -t authyservice .
docker run -it -p 9000:9000 -p 9443:9443 --rm authyservice
