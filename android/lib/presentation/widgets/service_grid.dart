import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../data/models/room_config.dart';

class ServiceGrid extends StatelessWidget {
  final List<HotelService> services;

  const ServiceGrid({super.key, required this.services});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        const Text(
          'HOTEL SERVICES',
          style: TextStyle(
            color: Colors.white38,
            fontSize: 11,
            letterSpacing: 4,
          ),
        ),
        const SizedBox(height: 12),
        FocusTraversalGroup(
          policy: OrderedTraversalPolicy(),
          child: SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 40),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                for (var i = 0; i < services.length; i++)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 8),
                    child: FocusTraversalOrder(
                      order: NumericFocusOrder(i.toDouble()),
                      child: _ServiceTile(
                        service: services[i],
                        autofocus: i == 0,
                      ),
                    ),
                  ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}

class _ServiceTile extends StatefulWidget {
  final HotelService service;
  final bool autofocus;

  const _ServiceTile({required this.service, this.autofocus = false});

  @override
  State<_ServiceTile> createState() => _ServiceTileState();
}

class _ServiceTileState extends State<_ServiceTile> {
  bool _focused = false;

  @override
  Widget build(BuildContext context) {
    return Focus(
      autofocus: widget.autofocus,
      onFocusChange: (v) => setState(() => _focused = v),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        width: 140,
        height: 100,
        decoration: BoxDecoration(
          color: _focused
              ? const Color(0xFFD4AF37).withValues(alpha: 0.15)
              : Colors.black.withValues(alpha: 0.4),
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: _focused ? const Color(0xFFD4AF37) : Colors.white12,
            width: _focused ? 2 : 1,
          ),
          boxShadow: _focused
              ? [
                  BoxShadow(
                    color: const Color(0xFFD4AF37).withValues(alpha: 0.3),
                    blurRadius: 16,
                    spreadRadius: 2,
                  )
                ]
              : null,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (widget.service.iconUrl != null)
              CachedNetworkImage(
                imageUrl: widget.service.iconUrl!,
                width: 36,
                height: 36,
                color: Colors.white,
                errorWidget: (_, _, _) => const Icon(
                  Icons.room_service,
                  color: Colors.white70,
                  size: 36,
                ),
              )
            else
              Icon(
                Icons.room_service,
                color: _focused ? const Color(0xFFD4AF37) : Colors.white70,
                size: 36,
              ),
            const SizedBox(height: 8),
            Text(
              widget.service.name,
              style: TextStyle(
                color: _focused ? const Color(0xFFD4AF37) : Colors.white70,
                fontSize: 12,
                fontWeight:
                    _focused ? FontWeight.w600 : FontWeight.w400,
              ),
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ),
      ),
    );
  }
}
