package com.candlekart.inventory_service.controller;

import com.candlekart.inventory_service.dto.InventoryRequest;
import com.candlekart.inventory_service.dto.InventoryResponse;
import com.candlekart.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {


}