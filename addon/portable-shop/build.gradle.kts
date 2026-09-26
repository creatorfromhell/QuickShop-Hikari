plugins {
    id("quickshop.compat-conventions")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(project(":compatibility:common"))
    compileOnly(project(":quickshop-bukkit"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>().configureEach {
    archiveBaseName.set("Addon-PortableShop")
}
