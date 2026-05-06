package com.example.sas.controller;

import com.example.sas.entity.Appointment;
import com.example.sas.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/refunds")
@RequiredArgsConstructor
public class AdminRefundController {

    private final AppointmentRepository appointmentRepository;

    @GetMapping
    public String refundsPage(Model model) {
        List<Appointment> cancelledAll = appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getStatus() == Appointment.Status.CANCELLED)
                .filter(a -> a.getRefundBankName() != null)
                .collect(Collectors.toList());

        List<Appointment> pendingRefunds = cancelledAll.stream()
                .filter(a -> !a.isRefundCompleted())
                .collect(Collectors.toList());

        List<Appointment> completedRefunds = cancelledAll.stream()
                .filter(a -> a.isRefundCompleted())
                .collect(Collectors.toList());

        model.addAttribute("pendingRefunds", pendingRefunds);
        model.addAttribute("completedRefunds", completedRefunds);
        return "admin/refunds";
    }

    @PostMapping("/{id}/complete")
    public String markRefundCompleted(@PathVariable("id") Long id, RedirectAttributes flash) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setRefundCompleted(true);
        appointmentRepository.save(appointment);
        
        flash.addFlashAttribute("success", "Refund marked as completed for Appointment #" + id);
        return "redirect:/admin/refunds";
    }
}
