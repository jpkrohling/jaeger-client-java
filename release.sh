#!/bin/bash

gpg --no-tty --batch --allow-secret-key-import --import signing-key.asc
if (( $? != 0 )); then
    echo "Failed to import the signing key into the GPG secring."
    exit 1
fi

./gradlew \
    -Psigning.keyId="${SIGNING_KEY_ID}" \
    -Psigning.password="${SIGNING_KEY_PASSPHRASE}" \
    -PossrhUsername="${SONATYPE_USERNAME}" \
    -PossrhPassword="${SONATYPE_PASSWORD}" \
    -Psigning.secretKeyRingFile="${HOME}/.gnupg/secring.gpg" \
    upload

exit $?