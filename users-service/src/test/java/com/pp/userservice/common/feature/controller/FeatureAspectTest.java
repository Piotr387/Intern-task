package com.pp.userservice.common.feature.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// start L4 test 2
@ExtendWith(MockitoExtension.class)
public class FeatureAspectTest {

  private FeatureAspect featureAspect;

  @Mock
  private FeatureService featureService;

  @Mock
  private ProceedingJoinPoint joinPoint;

  @BeforeEach
  public void setUp() {
    featureAspect = new FeatureAspect(featureService);
  }

  @Test
  public void testCheckFeature_whenFeatureEnabled_shouldProceed() throws Throwable {
    // given
    when(featureService.isFeatureEnabled(FeatureVersions.REGISTER_USER)).thenReturn(true);
    when(joinPoint.proceed()).thenReturn("Success");

    // when
    Object result = featureAspect.checkFeature(joinPoint,
        createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.THROW_EXCEPTION));

    // then
    assertEquals("Success", result);
    verify(joinPoint).proceed();
  }

  @Test
  public void testCheckFeature_whenFeatureDisabledAndThrowExceptionMode_shouldThrowException()
      throws Throwable {
    // given
    when(featureService.isFeatureEnabled(FeatureVersions.REGISTER_USER)).thenReturn(false);

    // when & then
    FeatureException exception = assertThrows(FeatureException.class, () -> {
      featureAspect.checkFeature(joinPoint,
          createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.THROW_EXCEPTION));
    });

    assertEquals("Feature REGISTER_USER is disabled on this environment!", exception.getMessage());
    verify(joinPoint, never()).proceed();
  }

  @Test
  public void testCheckFeature_whenFeatureDisabledAndIgnoreMode_shouldNotProceed()
      throws Throwable {
    // given
    when(featureService.isFeatureEnabled(FeatureVersions.REGISTER_USER)).thenReturn(false);

    // when
    Object result = featureAspect.checkFeature(joinPoint,
        createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.IGNORE));

    // then
    assertNull(result);
    verify(joinPoint, never()).proceed();
  }

  @Test
  public void testCheckFeature_whenFeatureDisabledThenReevaluatedWithIgnoreMode_shouldNotThrowException()
      throws Throwable {
    // given
    when(featureService.isFeatureEnabled(FeatureVersions.REGISTER_USER)).thenReturn(false);

    // when
    FeatureException exception = assertThrows(FeatureException.class, () -> {
      featureAspect.checkFeature(joinPoint,
          createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.THROW_EXCEPTION));
    });

    // then
    assertEquals("Feature REGISTER_USER is disabled on this environment!", exception.getMessage());
    verify(joinPoint, never()).proceed();

    // when
    Object result = featureAspect.checkFeature(joinPoint,
        createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.IGNORE));

    // then
    assertNull(result);
    verify(joinPoint, never()).proceed();
  }

  @Test
  public void testCheckFeature_forMultipleFeatures_shouldHandleCorrectly() throws Throwable {
    // given
    when(featureService.isFeatureEnabled(FeatureVersions.REGISTER_USER)).thenReturn(true);
    when(joinPoint.proceed()).thenReturn("User added");

    // when
    Object result1 = featureAspect.checkFeature(joinPoint, createFeatureController(FeatureVersions.REGISTER_USER, FeatureMode.THROW_EXCEPTION));

    // then
    assertEquals("User added", result1);
    verify(joinPoint, times(1)).proceed(); // Verify proceed() is called for REGISTER_USER

    // given
    when(featureService.isFeatureEnabled(FeatureVersions.SIGN_FOR_LECTURE)).thenReturn(false);

    // when & then
    FeatureException exception = assertThrows(FeatureException.class, () -> {
      featureAspect.checkFeature(joinPoint, createFeatureController(FeatureVersions.SIGN_FOR_LECTURE, FeatureMode.THROW_EXCEPTION));
    });

    assertEquals("Feature SIGN_FOR_LECTURE is disabled on this environment!", exception.getMessage());
    verifyNoMoreInteractions(joinPoint); // Confirm proceed() isn't called for disabled SIGN_FOR_LECTURE
  }


  private FeatureController createFeatureController(FeatureVersions featureVersions,
      FeatureMode featureMode) {
    return new FeatureController() {
      @Override
      public FeatureVersions value() {
        return featureVersions;
      }

      @Override
      public FeatureMode featureMode() {
        return featureMode;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return FeatureController.class;
      }
    };
  }
}
