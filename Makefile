-include jaeger-crossdock/rules.mk

GRADLE=GRADLE_OPTS=-Xmx1g ./gradlew

.PHONY: clean
clean:
	$(GRADLE) clean

.PHONY: test
test:
	$(GRADLE) check

.PHONY: test-travis
test-travis:
	$(GRADLE) -is check

.PHONY: release
release:
	./travis/release.sh

.PHONY: publish-release
publish-snapshot:
	$(GRADLE) bintrayUpload

.PHONY: publish-snapshot
publish-snapshot:
	$(GRADLE) artifactoryPublish

.PHONY: coverage
coverage: SHELL:=/bin/bash
coverage:
	$(GRADLE) codeCoverageReport
	bash <(curl -s https://codecov.io/bash)
