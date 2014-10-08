DESCRIPTION = "Lexical analyzer generator for Java"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8e2372bdbf22c99279ae4599a13cc458"

BBCLASSEXTEND = "native"

inherit java-library

SRC_URI = "\
	https://github.com/mozilla/rhino/archive/Rhino1_7R4_RELEASE.zip \
	file://rhino \
	file://rhino-jsc \
	"

S = "${WORKDIR}/rhino-Rhino1_7R4_RELEASE"

PACKAGES = "${JPN} rhino"

FILES_${PN} = "${bindir}/rhino ${bindir}/rhino-jsc"
RDEPENDS_${PN} = "java2-runtime ${JPN}"
RDEPENDS_${PN}_virtclass-native = ""

do_compile() {
	mkdir -p build

	# Compatibility fix for jamvm which has non-genericised
	# java.lang classes. :(
	bcp_arg="-bootclasspath ${STAGING_DATADIR_NATIVE}/classpath/glibj.zip"

	javac $bcp_arg -source 1.6 -sourcepath src -d build `find src -name "*.java"`

	mkdir -p build/org/mozilla/javascript/resources
	cp src/org/mozilla/javascript/resources/*.properties build/org/mozilla/javascript/resources

	fastjar cfm ${JARFILENAME} ${S}/src/manifest -C build .
}

do_install_append() {
	install -d ${D}${bindir}

	install -m 0755 ${WORKDIR}/rhino ${D}${bindir}
	install -m 0755 ${WORKDIR}/rhino-jsc ${D}${bindir}
}

SRC_URI[md5sum] = "d56e7ce505f96af1e00c5357e417177c"
SRC_URI[sha256sum] = "8a22e019211f53f761061983c9047f4fe423e3266fd41c51d6f27d305ea2189c"
