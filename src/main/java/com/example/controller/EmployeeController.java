package com.example.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Employee;
import com.example.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
    
    private volatile boolean stopStress = false;
    

    @GetMapping("/stress-cpu")
    public ResponseEntity<String> stressCpuAndMemory() {
        stopStress = false;

        // Thread for CPU stress
        Thread cpuStressThread = new Thread(() -> {
            try {
                while (!stopStress) {
                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        Math.pow(Math.random(), Math.random());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread for memory stress
        Thread memoryStressThread = new Thread(() -> {
            try {
                List<int[]> memoryHog = new ArrayList<>();
                while (!stopStress) {
                    // Allocate large arrays to consume memory
                    memoryHog.add(new int[1_000_000]); // Allocate 1 million integers (~4MB each)
                    Thread.sleep(100); // Gradual increase
                }
            } catch (OutOfMemoryError e) {
                System.err.println("Out of Memory!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Start both threads
        cpuStressThread.start();
        memoryStressThread.start();

        return ResponseEntity.ok("CPU and Memory stress started. App will crash or become unresponsive shortly.");
    }

    @GetMapping("/stop-stress")
    public ResponseEntity<String> stopStressCpu() {
        stopStress = true;
        return ResponseEntity.ok("CPU stress stopped.");
    }
}

