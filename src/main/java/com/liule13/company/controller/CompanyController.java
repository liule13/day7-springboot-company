package com.liule13.company.controller;

import com.liule13.company.entity.Company;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/companies")
@RestController
public class CompanyController {
    private final Map<Integer, Company> companies = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public void clear() {
        companies.clear();
        idGenerator.set(1);
    }

    @GetMapping
    public List<Company> getAllCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<Company> all = new ArrayList<>(companies.values());
        int start = (page - 1) * size;
        if (start >= all.size()) {
            return Collections.emptyList();
        }
        int end = Math.min(start + size, all.size());
        return all.subList(start, end);
    }
    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        Integer id = idGenerator.getAndIncrement();
        company.setId(id);
        companies.put(id, company);
        return company;
    }
    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Integer id) {
        return companies.get(id);
    }
}
