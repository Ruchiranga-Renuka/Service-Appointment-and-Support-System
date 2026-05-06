// =============================================
// Service Appointment & Support System
// Main JavaScript
// =============================================

document.addEventListener('DOMContentLoaded', () => {

    // ─── ICONS (Lucide) ───────────────────────
    // Sidebar fragments do not always include script tags, so initialize icons globally.
    const initLucideIcons = () => {
        if (window.lucide && typeof window.lucide.createIcons === 'function') {
            window.lucide.createIcons();
        }
    };

    if (window.lucide && typeof window.lucide.createIcons === 'function') {
        initLucideIcons();
    } else {
        let lucideScript = document.querySelector('script[data-lucide-cdn="true"]');
        if (!lucideScript) {
            lucideScript = document.createElement('script');
            lucideScript.src = 'https://unpkg.com/lucide@latest';
            lucideScript.setAttribute('data-lucide-cdn', 'true');
            lucideScript.onload = initLucideIcons;
            document.head.appendChild(lucideScript);
        } else {
            lucideScript.addEventListener('load', initLucideIcons, { once: true });
        }
    }

    // ─── SIDEBAR TOGGLE (mobile) ─────────────
    const sidebar = document.querySelector('.sidebar');
    const menuBtn = document.getElementById('menuToggle');
    const overlay = document.getElementById('sidebarOverlay');

    if (menuBtn) {
        menuBtn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
            overlay.classList.toggle('open');
        });
    }
    if (overlay) {
        overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('open');
        });
    }

    // ─── ACTIVE NAV ITEM ─────────────────────
    const navItems = document.querySelectorAll('.nav-item');
    const path = window.location.pathname;
    navItems.forEach(item => {
        const href = item.getAttribute('href') || '';
        if (href && path.startsWith(href)) {
            item.classList.add('active');
        }
    });

    // ─── MODAL HELPERS ───────────────────────
    window.openModal = (id) => {
        const m = document.getElementById(id);
        if (m) m.classList.add('open');
    };
    window.closeModal = (id) => {
        const m = document.getElementById(id);
        if (m) m.classList.remove('open');
    };

    // Close modal on overlay click
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) overlay.classList.remove('open');
        });
    });

    // ─── ROLE TABS (login page) ───────────────
    const roleTabs = document.querySelectorAll('.role-tab');
    roleTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            roleTabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            const roleInput = document.getElementById('roleInput');
            if (roleInput) roleInput.value = tab.dataset.role || '';
        });
    });

    // ─── PASSWORD TOGGLE ─────────────────────
    document.querySelectorAll('[data-pwd-toggle]').forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.dataset.pwdToggle;
            const input = document.getElementById(targetId);
            if (!input) return;
            if (input.type === 'password') {
                input.type = 'text';
                btn.textContent = '🙈';
            } else {
                input.type = 'password';
                btn.textContent = '👁️';
            }
        });
    });

    // ─── AUTO-DISMISS ALERTS ─────────────────
    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity .5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 600);
        }, 5000);
    });

    // ─── PREMIUM CONFIRM MODAL ────────────────
    const createConfirmModal = () => {
        let overlay = document.getElementById('premiumConfirmOverlay');
        if (!overlay) {
            overlay = document.createElement('div');
            overlay.id = 'premiumConfirmOverlay';
            overlay.className = 'confirm-overlay';
            overlay.innerHTML = `
                <div class="confirm-modal" style="max-width: 480px;">
                    <div class="confirm-icon"><i data-lucide="alert-triangle"></i></div>
                    <div class="confirm-title" id="confirmTitle">Cancel Appointment?</div>
                    <div class="confirm-text" id="confirmText" style="margin-bottom: 20px;">Are you sure? This action cannot be undone.</div>
                    
                    <div id="refundForm" style="display:none; text-align: left; margin-bottom: 24px; background: #f8fafc; padding: 16px; border-radius: 12px; border: 1px solid #e2e8f0;">
                        <p style="font-size: 12px; font-weight: 700; color: #475569; margin-bottom: 12px; text-transform: uppercase; letter-spacing: 0.5px;">Refund Bank Details</p>
                        <div style="display: grid; gap: 10px;">
                            <input type="text" id="refundBank" class="form-control" placeholder="Bank Name" style="min-height: 38px; font-size: 13px;">
                            <input type="text" id="refundAcc" class="form-control" placeholder="Account Number" style="min-height: 38px; font-size: 13px;">
                            <input type="text" id="refundHolder" class="form-control" placeholder="Account Holder Name" style="min-height: 38px; font-size: 13px;">
                            <input type="text" id="refundBranch" class="form-control" placeholder="Branch Name" style="min-height: 38px; font-size: 13px;">
                        </div>
                    </div>

                    <div class="confirm-actions">
                        <button class="confirm-btn confirm-btn-cancel" id="confirmCancelBtn">Wait, No</button>
                        <button class="confirm-btn confirm-btn-danger" id="confirmOkBtn">Yes, Cancel</button>
                    </div>
                </div>
            `;
            document.body.appendChild(overlay);
            if (window.lucide) window.lucide.createIcons({ entryPoints: [overlay] });
        }
        return overlay;
    };

    document.querySelectorAll('[data-confirm]').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const overlay = createConfirmModal();
            const title = document.getElementById('confirmTitle');
            const text = document.getElementById('confirmText');
            const okBtn = document.getElementById('confirmOkBtn');
            const cancelBtn = document.getElementById('confirmCancelBtn');
            const refundForm = document.getElementById('refundForm');

            // Customization based on data-confirm (if any)
            const customMsg = btn.dataset.confirm;
            if (customMsg && customMsg.toLowerCase().includes('cancel')) {
                title.textContent = "Cancel Appointment?";
                text.textContent = "To process your refund, please provide your bank account details below:";
                refundForm.style.display = 'block';
                okBtn.textContent = "Submit & Cancel";
            } else {
                title.textContent = "Please Confirm";
                text.textContent = customMsg || "Are you sure you want to proceed?";
                refundForm.style.display = 'none';
                okBtn.textContent = "Yes, Proceed";
            }

            overlay.classList.add('open');

            const cleanup = () => {
                overlay.classList.remove('open');
                okBtn.removeEventListener('click', onOk);
                cancelBtn.removeEventListener('click', onCancel);
            };

            const onOk = () => {
                const form = btn.closest('form');
                if (form && customMsg.toLowerCase().includes('cancel')) {
                    // Inject refund details as hidden inputs before submit
                    const bank = document.getElementById('refundBank').value;
                    const acc = document.getElementById('refundAcc').value;
                    const holder = document.getElementById('refundHolder').value;
                    const branch = document.getElementById('refundBranch').value;

                    if (!bank || !acc || !holder) {
                        alert("Please fill in your primary bank details for the refund.");
                        return;
                    }

                    const addHidden = (name, val) => {
                        const inv = document.createElement('input');
                        inv.type = 'hidden';
                        inv.name = name;
                        inv.value = val;
                        form.appendChild(inv);
                    };
                    addHidden('bankName', bank);
                    addHidden('accNo', acc);
                    addHidden('holderName', holder);
                    addHidden('branch', branch);
                }

                cleanup();
                if (form) form.submit();
                else if (btn.tagName === 'A') window.location.href = btn.href;
            };
            const onCancel = () => {
                cleanup();
            };

            okBtn.addEventListener('click', onOk);
            cancelBtn.addEventListener('click', onCancel);
        });
    });

    // ─── POPULATE EDIT MODAL ──────────────────
    document.querySelectorAll('[data-edit]').forEach(btn => {
        btn.addEventListener('click', () => {
            const data = JSON.parse(btn.dataset.edit || '{}');
            const modalId = btn.dataset.modal;
            const modal = document.getElementById(modalId);
            if (!modal) return;
            Object.entries(data).forEach(([key, val]) => {
                const el = modal.querySelector(`[name="${key}"]`);
                if (el) el.value = val;
            });
            openModal(modalId);
        });
    });

    // ─── SEARCH / FILTER TABLE ────────────────
    const searchInput = document.getElementById('tableSearch');
    if (searchInput) {
        searchInput.addEventListener('input', () => {
            const q = searchInput.value.toLowerCase();
            const rows = document.querySelectorAll('[data-searchable]');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(q) ? '' : 'none';
            });
        });
    }


  // ─── DATE: set min to today ───────────────
  document.querySelectorAll('input[type="date"]').forEach(input => {
    if (!input.min) {
      const today = new Date().toISOString().split('T')[0];
      input.min = today;
    }
  });

  // ─── TOOLTIP ──────────────────────────────
  document.querySelectorAll('[title]').forEach(el => {
    el.setAttribute('data-tip', el.getAttribute('title'));
    el.removeAttribute('title');
  });


// ─── PAYMENT SUMMARY CHARTS ────────────────
// Only runs when the three chart canvases exist (admin dashboard page).
    if (typeof Chart !== 'undefined' && document.getElementById('pscStatusChart')) {

        // Shared tooltip style matching the light theme
        const tooltipDefaults = {
            backgroundColor: '#1e293b',
            titleColor: '#f8fafc',
            bodyColor: '#cbd5e1',
            borderColor: '#334155',
            borderWidth: 1,
            padding: 10,
            cornerRadius: 8,
        };

        /* ── 1. PAYMENT STATUS — Donut ─────────────────────────────────── */
        const statusLabels = ['Pending', 'Receipt Uploaded', 'Confirmed', 'Paid', 'Failed', 'Refunded'];
        const statusData = [
            +document.getElementById('pscStatusChart').dataset.pending || 0,
            +document.getElementById('pscStatusChart').dataset.receiptUploaded || 0,
            +document.getElementById('pscStatusChart').dataset.confirmed || 0,
            +document.getElementById('pscStatusChart').dataset.paid || 0,
            +document.getElementById('pscStatusChart').dataset.failed || 0,
            +document.getElementById('pscStatusChart').dataset.refunded || 0,
        ];
        const statusColors = ['#f59e0b', '#3b82f6', '#06b6d4', '#10b981', '#ef4444', '#8b5cf6'];

        const totalPayments = statusData.reduce((a, b) => a + b, 0);
        const donutTotalEl = document.getElementById('pscDonutTotal');
        if (donutTotalEl) donutTotalEl.textContent = totalPayments;

        // Build legend — skip statuses with 0 count
        const legendEl = document.getElementById('pscStatusLegend');
        if (legendEl) {
            statusLabels.forEach((lbl, i) => {
                if (!statusData[i]) return;
                const item = document.createElement('div');
                item.className = 'psc-legend-item';
                item.innerHTML = `<span class="psc-dot" style="background:${statusColors[i]}"></span>${lbl} (${statusData[i]})`;
                legendEl.appendChild(item);
            });
        }

        new Chart(document.getElementById('pscStatusChart'), {
            type: 'doughnut',
            data: {
                labels: statusLabels,
                datasets: [{
                    data: statusData,
                    backgroundColor: statusColors,
                    borderColor: '#ffffff',
                    borderWidth: 2,
                    hoverOffset: 6,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '70%',
                plugins: {
                    legend: {display: false},
                    tooltip: {
                        ...tooltipDefaults,
                        callbacks: {
                            label: ctx => {
                                const pct = totalPayments > 0 ? Math.round(ctx.parsed / totalPayments * 100) : 0;
                                return ` ${ctx.label}: ${ctx.parsed} (${pct}%)`;
                            }
                        }
                    }
                }
            }
        });

        /* ── 2. REVENUE BY METHOD — Horizontal Bar ─────────────────────── */
        const methodCanvas = document.getElementById('pscMethodChart');
        const cardRev = parseFloat(methodCanvas.dataset.card || 0);
        const bankRev = parseFloat(methodCanvas.dataset.bank || 0);


        new Chart(methodCanvas, {
            type: 'bar',
            data: {
                labels: ['Card', 'Bank Transfer'],
                datasets: [{
                    label: 'Revenue (Rs.)',
                    data: [cardRev, bankRev],
                    backgroundColor: ['rgba(37,99,235,0.85)', 'rgba(37,99,235,0.55)', 'rgba(37,99,235,0.3)'],
                    borderColor: '#2563eb',
                    borderWidth: 1.5,
                    borderRadius: 6,
                    borderSkipped: false,
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {display: false},
                    tooltip: {
                        ...tooltipDefaults,
                        callbacks: {label: ctx => ` Rs. ${ctx.parsed.x.toLocaleString()}`}
                    }
                },
                scales: {
                    x: {
                        grid: {color: '#f1f5f9'},
                        ticks: {callback: v => v >= 1000 ? `Rs.${(v / 1000).toFixed(0)}k` : `Rs.${v}`}
                    },
                    y: {grid: {display: false}}
                }
            }
        });

        /* ── 3. MONTHLY REVENUE TREND — Area + dashed count line ────────── */
        const lineCanvas = document.getElementById('pscMonthlyChart');
        const monthLabels = JSON.parse(lineCanvas.dataset.labels || '["Jan","Feb","Mar","Apr","May","Jun"]');
        const monthRevenue = JSON.parse(lineCanvas.dataset.revenue || '[0,0,0,0,0,0]');
        const monthCount = JSON.parse(lineCanvas.dataset.count || '[0,0,0,0,0,0]');

        new Chart(lineCanvas, {
            type: 'line',
            data: {
                labels: monthLabels,
                datasets: [
                    {
                        label: 'Revenue (Rs.)',
                        data: monthRevenue,
                        yAxisID: 'yRev',
                        borderColor: '#2563eb',
                        backgroundColor: 'rgba(37,99,235,0.08)',
                        fill: true,
                        tension: 0.4,
                        pointBackgroundColor: '#2563eb',
                        pointRadius: 4,
                        pointHoverRadius: 6,
                        borderWidth: 2,
                    },
                    {
                        label: 'No. of Payments',
                        data: monthCount,
                        yAxisID: 'yCnt',
                        borderColor: '#10b981',
                        backgroundColor: 'transparent',
                        fill: false,
                        tension: 0.4,
                        pointBackgroundColor: '#10b981',
                        pointRadius: 3,
                        pointHoverRadius: 5,
                        borderWidth: 1.5,
                        borderDash: [4, 3],
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: {mode: 'index', intersect: false},
                plugins: {
                    legend: {display: false},
                    tooltip: {
                        ...tooltipDefaults,
                        callbacks: {
                            label: ctx => ctx.dataset.yAxisID === 'yRev'
                                ? ` Revenue: Rs. ${ctx.parsed.y.toLocaleString()}`
                                : ` Payments: ${ctx.parsed.y}`
                        }
                    }
                },
                scales: {
                    yRev: {
                        position: 'left',
                        grid: {color: '#f1f5f9'},
                        ticks: {callback: v => v >= 1000 ? `Rs.${(v / 1000).toFixed(0)}k` : `Rs.${v}`}
                    },
                    yCnt: {
                        position: 'right',
                        grid: {display: false},
                        ticks: {stepSize: 1}
                    },
                    x: {grid: {color: '#f1f5f9'}}
                }
            }
        });
    }


// ─── END PAYMENT SUMMARY CHARTS ───────────
//

});


