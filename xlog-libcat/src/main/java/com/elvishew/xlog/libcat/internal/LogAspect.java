/*
 * Copyright 2021 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elvishew.xlog.libcat.internal;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect all logging via {@link android.util.Log}.
 */
@Aspect
public class LogAspect {

  @Pointcut("within(com.elvishew.xlog..*)")
  public void withinXlog() {
  }

  @Pointcut("call(* android.util.Log.v(String, String))")
  public void call_Log_V_SS() {
  }

  @Pointcut("call(* android.util.Log.v(String, String, Throwable))")
  public void call_Log_V_SST() {
  }

  @Pointcut("call(* android.util.Log.d(String, String))")
  public void call_Log_D_SS() {
  }

  @Pointcut("call(* android.util.Log.d(String, String, Throwable))")
  public void call_Log_D_SST() {
  }

  @Pointcut("call(* android.util.Log.i(String, String))")
  public void call_Log_I_SS() {
  }

  @Pointcut("call(* android.util.Log.i(String, String, Throwable))")
  public void call_Log_I_SST() {
  }

  @Pointcut("call(* android.util.Log.w(String, String))")
  public void call_Log_W_SS() {
  }

  @Pointcut("call(* android.util.Log.w(String, String, Throwable))")
  public void call_Log_W_SST() {
  }

  @Pointcut("call(* android.util.Log.w(String, Throwable))")
  public void call_Log_W_ST() {
  }

  @Pointcut("call(* android.util.Log.e(String, String))")
  public void call_Log_E_SS() {
  }

  @Pointcut("call(* android.util.Log.e(String, String, Throwable))")
  public void call_Log_E_SST() {
  }

  @Pointcut("call(* android.util.Log.println(int, String, String))")
  public void call_Log_Println_ISS() {
  }

  @Around("call_Log_V_SS() && !withinXlog()")
  public int cat_Log_V_SS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.v((String) args[0], (String) args[1]);
  }

  @Around("call_Log_V_SST() && !withinXlog()")
  public int cat_Log_V_SST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.v((String) args[0], (String) args[1], (Throwable) args[2]);
  }

  @Around("call_Log_D_SS() && !withinXlog()")
  public int cat_Log_D_SS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.d((String) args[0], (String) args[1]);
  }

  @Around("call_Log_D_SST() && !withinXlog()")
  public int cat_Log_D_SST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.d((String) args[0], (String) args[1], (Throwable) args[2]);
  }

  @Around("call_Log_I_SS() && !withinXlog()")
  public int cat_Log_I_SS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.i((String) args[0], (String) args[1]);
  }

  @Around("call_Log_I_SST() && !withinXlog()")
  public int cat_Log_I_SST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.i((String) args[0], (String) args[1], (Throwable) args[2]);
  }

  @Around("call_Log_W_SS() && !withinXlog()")
  public int cat_Log_W_SS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.w((String) args[0], (String) args[1]);
  }

  @Around("call_Log_W_SST() && !withinXlog()")
  public int cat_Log_W_SST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.w((String) args[0], (String) args[1], (Throwable) args[2]);
  }

  @Around("call_Log_W_ST() && !withinXlog()")
  public int cat_Log_W_ST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.w((String) args[0], (Throwable) args[1]);
  }

  @Around("call_Log_E_SS() && !withinXlog()")
  public int cat_Log_E_SS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.e((String) args[0], (String) args[1]);
  }

  @Around("call_Log_E_SST() && !withinXlog()")
  public int cat_Log_E_SST(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.e((String) args[0], (String) args[1], (Throwable) args[2]);
  }

  @Around("call_Log_Println_ISS() && !withinXlog()")
  public int cat_Log_Println_ISS(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    return Cat.println((Integer) args[0], (String) args[1], (String) args[2]);
  }
}
