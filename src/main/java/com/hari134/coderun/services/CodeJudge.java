package com.hari134.coderun.services;

import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Component;



@Component
public class CodeJudge{
  private Semaphore semaphore = new Semaphore(50);

}
