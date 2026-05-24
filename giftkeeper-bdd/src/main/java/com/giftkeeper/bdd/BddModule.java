package com.giftkeeper.bdd;

/**
 * Marker class for the BDD module.
 *
 * <p>The executable value of this module is in its Cucumber test scenarios.
 * This class intentionally keeps the module as a normal Maven JAR module so
 * the test lifecycle remains active while avoiding an empty-JAR warning during
 * academic/CI builds.</p>
 */
public final class BddModule {
    private BddModule() {
    }
}
