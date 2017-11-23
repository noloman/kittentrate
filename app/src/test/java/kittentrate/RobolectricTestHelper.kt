package kittentrate

import android.app.Application
import android.content.Context
import manulorenzo.me.kittentrate.BuildConfig
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Created by Manuel Lorenzo on 18/11/2017.
 */
/**
 * Base class for Robolectric data layer tests.
 * Inherit from this class to create a test.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        application = RobolectricTestHelper.ApplicationStub::class,
        sdk = intArrayOf(21))
abstract class RobolectricTestHelper {
    fun context(): Context {
        return RuntimeEnvironment.application
    }

    internal class ApplicationStub : Application()
}