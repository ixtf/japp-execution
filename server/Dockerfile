# 测试环境运行
# docker run -it --rm -p 8080:8080 -p 9990:9990 wjh/execution

FROM jboss/wildfly

ADD execution.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]